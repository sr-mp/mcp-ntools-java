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
}
