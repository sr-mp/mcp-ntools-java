package com.example.mcp.tool;

import com.example.mcp.tool.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.ByteArrayInputStream;

@Service
public class NmapMcpService {
    @Autowired
    private NmapService nmapService;

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

    public ResolveHostnameResponse resolveHostname(ResolveHostnameRequest req) {
        // For now, use Java DNS lookup
        try {
            java.net.InetAddress addr = java.net.InetAddress.getByName(req.getIpAddress());
            return new ResolveHostnameResponse(addr.getHostName());
        } catch (Exception e) {
            return new ResolveHostnameResponse("error: " + e.getMessage());
        }
    }

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

    public NmapAdvancedScanResponse advancedScan(NmapAdvancedScanRequest req) {
        // Input validation
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
