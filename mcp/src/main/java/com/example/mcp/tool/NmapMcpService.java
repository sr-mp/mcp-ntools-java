package com.example.mcp.tool;

import com.example.mcp.tool.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.ByteArrayInputStream;

/**
 * Service class implementing all Nmap-based tool logic for the MCP protocol.
 * <p>
 * Provides methods for host discovery, hostname resolution, port scanning, multi-host scanning,
 * and advanced Nmap scans. Handles argument construction, output parsing, and error handling.
 * <p>
 * Why: Centralizes Nmap tool logic for reuse by controllers and to keep business logic out of REST endpoints.
 */
@Service
public class NmapMcpService {
    @Autowired
    private NmapService nmapService;

    /**
     * Injects the NmapService dependency (used for testability).
     */
    void setNmapService(NmapService nmapService) {
        this.nmapService = nmapService;
    }

    /**
     * Discovers live hosts in a network range using Nmap.
     * Parses XML output and returns discovered hosts.
     */
    public DiscoverHostsResponse discoverHosts(DiscoverHostsRequest req) {
        try {
            List<String> args = new ArrayList<>();
            args.add("-sn");
            args.addAll(Arrays.asList(req.getNetworkRange().trim().split("\\s+")));
            args.add(0, "-oX");
            args.add(1, "-");
            ToolCommandResponse resp = nmapService.runNmap(args.toArray(new String[0])).get();
            System.out.println("DEBUG RAW NMAP OUTPUT:\n" + resp.getResult());
            // Remove any leading non-XML output (e.g., warnings, banner)
            String xml = resp.getResult().replaceFirst("(?s)^.*?(<\\?xml)", "$1");
            // Parse XML output for discovered hosts
            List<String> hosts = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            NodeList hostNodes = doc.getElementsByTagName("host");
            for (int i = 0; i < hostNodes.getLength(); i++) {
                Element hostElem = (Element) hostNodes.item(i);
                NodeList statusList = hostElem.getElementsByTagName("status");
                if (statusList.getLength() > 0 && ((Element)statusList.item(0)).getAttribute("state").equals("up")) {
                    NodeList hostnames = hostElem.getElementsByTagName("hostname");
                    if (hostnames.getLength() > 0) {
                        hosts.add(((Element)hostnames.item(0)).getAttribute("name"));
                    } else {
                        NodeList addresses = hostElem.getElementsByTagName("address");
                        for (int j = 0; j < addresses.getLength(); j++) {
                            Element addrElem = (Element) addresses.item(j);
                            if ("ipv4".equals(addrElem.getAttribute("addrtype"))) {
                                hosts.add(addrElem.getAttribute("addr"));
                                break;
                            }
                        }
                    }
                }
            }
            return new DiscoverHostsResponse(hosts);
        } catch (Exception e) {
            return new DiscoverHostsResponse(List.of("error: " + e.getMessage()));
        }
    }

    /**
     * Resolves an IP address to a hostname using Java DNS lookup.
     */
    public ResolveHostnameResponse resolveHostname(ResolveHostnameRequest req) {
        try {
            java.net.InetAddress addr = java.net.InetAddress.getByName(req.getIpAddress());
            return new ResolveHostnameResponse(addr.getHostName());
        } catch (Exception e) {
            return new ResolveHostnameResponse("error: " + e.getMessage());
        }
    }

    /**
     * Scans specific ports on a single host using Nmap and parses open ports from XML output.
     */
    public ScanPortsResponse scanPorts(ScanPortsRequest req) {
        try {
            List<String> args = new ArrayList<>();
            args.add("-oX");
            args.add("-");
            args.add(req.getIpAddress());
            if (req.getPorts() != null && !req.getPorts().isEmpty()) {
                args.add("-p");
                args.add(req.getPorts());
            }
            ToolCommandResponse resp = nmapService.runNmap(args.toArray(new String[0])).get();
            System.out.println("DEBUG RAW NMAP OUTPUT:\n" + resp.getResult());
            // Remove any leading non-XML output (e.g., warnings, banner)
            String xml = resp.getResult().replaceFirst("(?s)^.*?(<\\?xml)", "$1");
            // Parse XML output for open ports
            Map<String, Object> result = new HashMap<>();
            List<String> openPorts = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            NodeList portNodes = doc.getElementsByTagName("port");
            for (int i = 0; i < portNodes.getLength(); i++) {
                Element portElem = (Element) portNodes.item(i);
                String portid = portElem.getAttribute("portid");
                String proto = portElem.getAttribute("protocol");
                NodeList stateNodes = portElem.getElementsByTagName("state");
                if (stateNodes.getLength() > 0 && ((Element)stateNodes.item(0)).getAttribute("state").equals("open")) {
                    openPorts.add(portid + "/" + proto);
                }
            }
            result.put("openPorts", openPorts);
            return new ScanPortsResponse(result);
        } catch (Exception e) {
            return new ScanPortsResponse(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Scans multiple hosts for open ports using Nmap and parses results per host.
     */
    public ScanMultipleHostsResponse scanMultipleHosts(ScanMultipleHostsRequest req) {
        try {
            List<String> args = new ArrayList<>();
            args.add("-oX");
            args.add("-");
            args.addAll(req.getIpList());
            if (req.getPorts() != null && !req.getPorts().isEmpty()) {
                args.add("-p");
                args.add(req.getPorts());
            }
            ToolCommandResponse resp = nmapService.runNmap(args.toArray(new String[0])).get();
            System.out.println("DEBUG RAW NMAP OUTPUT:\n" + resp.getResult());
            // Remove any leading non-XML output (e.g., warnings, banner)
            String xml = resp.getResult().replaceFirst("(?s)^.*?(<\\?xml)", "$1");
            // Parse XML output for each host's open ports
            Map<String, List<String>> hostPorts = new HashMap<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            NodeList hostNodes = doc.getElementsByTagName("host");
            for (int i = 0; i < hostNodes.getLength(); i++) {
                Element hostElem = (Element) hostNodes.item(i);
                String hostLabel = null;
                NodeList hostnames = hostElem.getElementsByTagName("hostname");
                if (hostnames.getLength() > 0) {
                    hostLabel = ((Element)hostnames.item(0)).getAttribute("name");
                } else {
                    NodeList addresses = hostElem.getElementsByTagName("address");
                    for (int j = 0; j < addresses.getLength(); j++) {
                        Element addrElem = (Element) addresses.item(j);
                        if ("ipv4".equals(addrElem.getAttribute("addrtype"))) {
                            hostLabel = addrElem.getAttribute("addr");
                            break;
                        }
                    }
                }
                List<String> openPorts = new ArrayList<>();
                NodeList portNodes = hostElem.getElementsByTagName("port");
                for (int j = 0; j < portNodes.getLength(); j++) {
                    Element portElem = (Element) portNodes.item(j);
                    String portid = portElem.getAttribute("portid");
                    String proto = portElem.getAttribute("protocol");
                    NodeList stateNodes = portElem.getElementsByTagName("state");
                    if (stateNodes.getLength() > 0 && ((Element)stateNodes.item(0)).getAttribute("state").equals("open")) {
                        openPorts.add(portid + "/" + proto);
                    }
                }
                if (hostLabel != null) {
                    hostPorts.put(hostLabel, openPorts);
                }
            }
            return new ScanMultipleHostsResponse(Map.of("hostPorts", hostPorts));
        } catch (Exception e) {
            return new ScanMultipleHostsResponse(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Performs an advanced Nmap scan with many options, parses results, and handles errors.
     */
    public NmapAdvancedScanResponse advancedScan(NmapAdvancedScanRequest req) {
        // Input validation
        if (nmapService == null) {
            throw new IllegalStateException("nmapService is not set. Did you forget to inject or mock it in your test?");
        }
        if (req.getTargets() == null || req.getTargets().isEmpty()) {
            return new NmapAdvancedScanResponse(null, null, false, "At least one target must be specified.");
        }
        if (req.getTiming() != null && (req.getTiming() < 0 || req.getTiming() > 5)) {
            return new NmapAdvancedScanResponse(null, null, false, "Timing must be between 0 and 5.");
        }
        if (Boolean.TRUE.equals(req.getUseNseScripts()) && req.getNseScripts() != null && req.getNseScripts().contains("default") && req.getNseScripts().size() > 1) {
            return new NmapAdvancedScanResponse(null, null, false, "Cannot combine 'default' NSE script with others.");
        }
        List<String> args = new ArrayList<>();
        // Output format
        String outputFormat = req.getOutputFormat() != null ? req.getOutputFormat() : "xml";
        if (outputFormat.equalsIgnoreCase("xml")) {
            args.add("-oX"); args.add("-");
        } else if (outputFormat.equalsIgnoreCase("json")) {
            args.add("-oJ"); args.add("-");
        } else if (outputFormat.equalsIgnoreCase("grepable")) {
            args.add("-oG"); args.add("-");
        }
        // IPv6
        if (Boolean.TRUE.equals(req.getIpv6())) args.add("-6");
        // Scan type
        if (req.getScanType() != null) {
            switch (req.getScanType().toUpperCase()) {
                case "SYN": args.add("-sS"); break;
                case "UDP": args.add("-sU"); break;
                case "AGGRESSIVE": args.add("-A"); break;
                default: break;
            }
        }
        // OS detection
        if (Boolean.TRUE.equals(req.getOsDetection())) args.add("-O");
        // Service/version detection
        if (Boolean.TRUE.equals(req.getServiceVersionDetection())) args.add("-sV");
        // Traceroute
        if (Boolean.TRUE.equals(req.getTraceroute())) args.add("--traceroute");
        // NSE scripts
        if (Boolean.TRUE.equals(req.getUseNseScripts())) {
            if (req.getNseScripts() != null && !req.getNseScripts().isEmpty()) {
                args.add("--script");
                args.add(String.join(",", req.getNseScripts()));
            } else {
                args.add("-sC");
            }
            if (req.getNseScriptArgs() != null && !req.getNseScriptArgs().isEmpty()) {
                for (Map.Entry<String, String> entry : req.getNseScriptArgs().entrySet()) {
                    args.add("--script-args");
                    args.add(entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        // Timing
        if (req.getTiming() != null) args.add("-T" + req.getTiming());
        // Extra flags
        if (req.getExtraFlags() != null) args.addAll(req.getExtraFlags());
        // Ports
        if (req.getPorts() != null && !req.getPorts().isEmpty()) {
            args.add("-p");
            args.add(req.getPorts());
        }
        // Targets
        if (req.getTargets() != null) args.addAll(req.getTargets());
        // --- New advanced features ---
        // Decoy scan
        if (req.getDecoys() != null && !req.getDecoys().isEmpty()) {
            args.add("-D");
            args.add(String.join(",", req.getDecoys()));
        }
        // Fragmentation
        if (Boolean.TRUE.equals(req.getFragmentPackets())) args.add("-f");
        // Source port spoofing
        if (req.getSourcePort() != null) {
            args.add("--source-port");
            args.add(req.getSourcePort().toString());
        }
        // Packet trace
        if (Boolean.TRUE.equals(req.getPacketTrace())) args.add("--packet-trace");
        // Debug level
        if (req.getDebugLevel() != null && req.getDebugLevel() > 0) {
            StringBuilder dbg = new StringBuilder("-");
            for (int i = 0; i < req.getDebugLevel(); i++) dbg.append("d");
            args.add(dbg.toString());
        }
        // ARP scan
        if (Boolean.TRUE.equals(req.getArpScan())) args.add("-PR");
        // IP protocol scan
        if (Boolean.TRUE.equals(req.getIpProtocolScan())) args.add("-sO");
        // Min/max rate
        if (req.getMinRate() != null) {
            args.add("--min-rate");
            args.add(req.getMinRate().toString());
        }
        if (req.getMaxRate() != null) {
            args.add("--max-rate");
            args.add(req.getMaxRate().toString());
        }
        // Host timeout
        if (req.getHostTimeout() != null) {
            args.add("--host-timeout");
            args.add(req.getHostTimeout() + "ms");
        }
        // All output formats at once (ignored in container, but for API completeness)
        if (Boolean.TRUE.equals(req.getAllOutputFormats())) {
            args.add("-oA");
            args.add("/tmp/mcp-nmap-output"); // Will not persist in container
        }
        // Custom output file (ignored in container)
        if (req.getCustomOutputFile() != null) {
            args.add("-oN");
            args.add(req.getCustomOutputFile());
        }
        // NSE script categories
        if (req.getNseCategories() != null && !req.getNseCategories().isEmpty()) {
            args.add("--script");
            args.add(String.join(",", req.getNseCategories()));
        }
        // NSE script update
        if (Boolean.TRUE.equals(req.getUpdateNseDb())) args.add("--script-updatedb");
        // Banner grabbing
        if (Boolean.TRUE.equals(req.getBannerGrab())) {
            args.add("--script");
            args.add("banner");
        }
        // Scan delay
        if (req.getScanDelay() != null) {
            args.add("--scan-delay");
            args.add(req.getScanDelay() + "ms");
        }
        if (req.getMaxScanDelay() != null) {
            args.add("--max-scan-delay");
            args.add(req.getMaxScanDelay() + "ms");
        }
        // Custom payloads
        if (req.getDataLength() != null) {
            args.add("--data-length");
            args.add(req.getDataLength().toString());
        }
        if (req.getCustomData() != null) {
            args.add("--data-string");
            args.add(req.getCustomData());
        }
        // Exclude/include hosts
        if (req.getExcludeHosts() != null && !req.getExcludeHosts().isEmpty()) {
            args.add("--exclude");
            args.add(String.join(",", req.getExcludeHosts()));
        }
        if (req.getExcludeFile() != null) {
            args.add("--excludefile");
            args.add(req.getExcludeFile());
        }
        // Traceroute enhancements
        if (Boolean.TRUE.equals(req.getRecordRoute())) args.add("--record-route");
        try {
            ToolCommandResponse resp = nmapService.runNmap(args.toArray(new String[0])).get();
            String raw = resp.getResult();
            boolean success = resp.isSuccess();
            String error = null;
            Map<String, Object> result = new HashMap<>();
            // Parse output if XML
            if (outputFormat.equalsIgnoreCase("xml")) {
                String xml = raw.replaceFirst("(?s)^.*?(<\\?xml)", "$1");
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
                    // Open ports per host
                    NodeList hostNodes = doc.getElementsByTagName("host");
                    Map<String, List<String>> hostPorts = new HashMap<>();
                    Map<String, String> osMatches = new HashMap<>();
                    Map<String, List<String>> serviceVersions = new HashMap<>();
                    Map<String, List<String>> traceroutes = new HashMap<>();
                    Map<String, List<String>> nseResults = new HashMap<>();
                    for (int i = 0; i < hostNodes.getLength(); i++) {
                        Element hostElem = (Element) hostNodes.item(i);
                        String hostLabel = null;
                        NodeList hostnames = hostElem.getElementsByTagName("hostname");
                        if (hostnames.getLength() > 0) {
                            hostLabel = ((Element)hostnames.item(0)).getAttribute("name");
                        } else {
                            NodeList addresses = hostElem.getElementsByTagName("address");
                            for (int j = 0; j < addresses.getLength(); j++) {
                                Element addrElem = (Element) addresses.item(j);
                                if (addrElem.hasAttribute("addr")) {
                                    hostLabel = addrElem.getAttribute("addr");
                                    break;
                                }
                            }
                        }
                        List<String> openPorts = new ArrayList<>();
                        List<String> services = new ArrayList<>();
                        List<String> nse = new ArrayList<>();
                        NodeList portNodes = hostElem.getElementsByTagName("port");
                        for (int j = 0; j < portNodes.getLength(); j++) {
                            Element portElem = (Element) portNodes.item(j);
                            String portid = portElem.getAttribute("portid");
                            String proto = portElem.getAttribute("protocol");
                            NodeList stateNodes = portElem.getElementsByTagName("state");
                            if (stateNodes.getLength() > 0 && ((Element)stateNodes.item(0)).getAttribute("state").equals("open")) {
                                openPorts.add(portid + "/" + proto);
                                // Service version
                                NodeList serviceNodes = portElem.getElementsByTagName("service");
                                if (serviceNodes.getLength() > 0) {
                                    Element serviceElem = (Element) serviceNodes.item(0);
                                    String name = serviceElem.getAttribute("name");
                                    String version = serviceElem.getAttribute("version");
                                    services.add(portid + ":" + name + (version.isEmpty() ? "" : (" " + version)));
                                }
                                // NSE script output
                                NodeList scriptNodes = portElem.getElementsByTagName("script");
                                for (int k = 0; k < scriptNodes.getLength(); k++) {
                                    Element scriptElem = (Element) scriptNodes.item(k);
                                    String id = scriptElem.getAttribute("id");
                                    String output = scriptElem.getAttribute("output");
                                    nse.add(portid + ":" + id + "=" + output);
                                }
                            }
                        }
                        // OS detection
                        NodeList osNodes = hostElem.getElementsByTagName("osmatch");
                        if (osNodes.getLength() > 0) {
                            Element osElem = (Element) osNodes.item(0);
                            osMatches.put(hostLabel, osElem.getAttribute("name"));
                        }
                        // Traceroute
                        NodeList traceNodes = hostElem.getElementsByTagName("trace");
                        if (traceNodes.getLength() > 0) {
                            List<String> hops = new ArrayList<>();
                            Element traceElem = (Element) traceNodes.item(0);
                            NodeList hopNodes = traceElem.getElementsByTagName("hop");
                            for (int h = 0; h < hopNodes.getLength(); h++) {
                                Element hopElem = (Element) hopNodes.item(h);
                                hops.add(hopElem.getAttribute("ipaddr"));
                            }
                            traceroutes.put(hostLabel, hops);
                        }
                        if (hostLabel != null) {
                            hostPorts.put(hostLabel, openPorts);
                            if (!services.isEmpty()) serviceVersions.put(hostLabel, services);
                            if (!nse.isEmpty()) nseResults.put(hostLabel, nse);
                        }
                    }
                    result.put("hostPorts", hostPorts);
                    if (!osMatches.isEmpty()) result.put("osMatches", osMatches);
                    if (!serviceVersions.isEmpty()) result.put("serviceVersions", serviceVersions);
                    if (!traceroutes.isEmpty()) result.put("traceroutes", traceroutes);
                    if (!nseResults.isEmpty()) result.put("nseResults", nseResults);
                } catch (Exception ex) {
                    error = "XML parse error: " + ex.getMessage();
                }
            }
            return new NmapAdvancedScanResponse(result, raw, success, error);
        } catch (Exception e) {
            return new NmapAdvancedScanResponse(null, null, false, e.getMessage());
        }
    }
}
