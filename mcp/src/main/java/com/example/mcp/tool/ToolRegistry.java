package com.example.mcp.tool;

import java.util.*;

/**
 * Registry for all available tools in the MCP server.
 * <p>
 * Maintains a mapping of tool names to their metadata, including descriptions and input/output schemas.
 * <p>
 * Why: Enables dynamic tool discovery and schema description for MCP protocol compliance and agent integration.
 */
public class ToolRegistry {
    private static final Map<String, ToolMetadata> tools = new LinkedHashMap<>();

    static {
        // Nmap Advanced Scan
        Map<String, Object> nmapAdvInput = new LinkedHashMap<>();
        nmapAdvInput.put("targets", Map.of("type", "array", "items", Map.of("type", "string"), "description", "Target IPs, hostnames, or ranges", "required", true));
        nmapAdvInput.put("scanType", Map.of("type", "string", "description", "Scan type: SYN, UDP, AGGRESSIVE, etc."));
        nmapAdvInput.put("osDetection", Map.of("type", "boolean", "description", "Enable OS detection"));
        nmapAdvInput.put("serviceVersionDetection", Map.of("type", "boolean", "description", "Enable service version detection"));
        nmapAdvInput.put("traceroute", Map.of("type", "boolean", "description", "Enable traceroute"));
        nmapAdvInput.put("useNseScripts", Map.of("type", "boolean", "description", "Use NSE scripts"));
        nmapAdvInput.put("nseScripts", Map.of("type", "array", "items", Map.of("type", "string"), "description", "NSE script names"));
        nmapAdvInput.put("nseScriptArgs", Map.of("type", "object", "additionalProperties", Map.of("type", "string"), "description", "NSE script arguments"));
        nmapAdvInput.put("outputFormat", Map.of("type", "string", "description", "Output format: xml, json, grepable"));
        nmapAdvInput.put("timing", Map.of("type", "integer", "description", "Timing template (0-5)"));
        nmapAdvInput.put("extraFlags", Map.of("type", "array", "items", Map.of("type", "string"), "description", "Extra Nmap flags"));
        nmapAdvInput.put("ipv6", Map.of("type", "boolean", "description", "Enable IPv6 scan"));
        nmapAdvInput.put("ports", Map.of("type", "string", "description", "Ports to scan, e.g. '80,443'"));
        nmapAdvInput.put("decoys", Map.of("type", "array", "items", Map.of("type", "string"), "description", "Decoy IPs for -D"));
        nmapAdvInput.put("fragmentPackets", Map.of("type", "boolean", "description", "Enable packet fragmentation (-f)"));
        nmapAdvInput.put("sourcePort", Map.of("type", "integer", "description", "Source port for scan"));
        nmapAdvInput.put("packetTrace", Map.of("type", "boolean", "description", "Enable packet trace"));
        nmapAdvInput.put("debugLevel", Map.of("type", "integer", "description", "Debug level (-d)"));
        nmapAdvInput.put("arpScan", Map.of("type", "boolean", "description", "Enable ARP scan (-PR)"));
        nmapAdvInput.put("ipProtocolScan", Map.of("type", "boolean", "description", "Enable IP protocol scan (-sO)"));
        nmapAdvInput.put("minRate", Map.of("type", "integer", "description", "Minimum packet rate"));
        nmapAdvInput.put("maxRate", Map.of("type", "integer", "description", "Maximum packet rate"));
        nmapAdvInput.put("hostTimeout", Map.of("type", "integer", "description", "Host timeout (ms)"));
        nmapAdvInput.put("allOutputFormats", Map.of("type", "boolean", "description", "Output all formats (-oA)"));
        nmapAdvInput.put("customOutputFile", Map.of("type", "string", "description", "Custom output file name"));
        nmapAdvInput.put("nseCategories", Map.of("type", "array", "items", Map.of("type", "string"), "description", "NSE script categories"));
        nmapAdvInput.put("updateNseDb", Map.of("type", "boolean", "description", "Update NSE script DB"));
        nmapAdvInput.put("bannerGrab", Map.of("type", "boolean", "description", "Enable banner grabbing"));
        nmapAdvInput.put("scanDelay", Map.of("type", "integer", "description", "Scan delay (ms)"));
        nmapAdvInput.put("maxScanDelay", Map.of("type", "integer", "description", "Max scan delay (ms)"));
        nmapAdvInput.put("dataLength", Map.of("type", "integer", "description", "Custom data length"));
        nmapAdvInput.put("customData", Map.of("type", "string", "description", "Custom data string"));
        nmapAdvInput.put("excludeHosts", Map.of("type", "array", "items", Map.of("type", "string"), "description", "Hosts to exclude"));
        nmapAdvInput.put("excludeFile", Map.of("type", "string", "description", "File with hosts to exclude"));
        nmapAdvInput.put("recordRoute", Map.of("type", "boolean", "description", "Record route"));
        Map<String, Object> nmapAdvOutput = new LinkedHashMap<>();
        nmapAdvOutput.put("result", Map.of("type", "object", "description", "Parsed scan results (hostPorts, osMatches, etc)"));
        nmapAdvOutput.put("rawOutput", Map.of("type", "string", "description", "Raw Nmap output"));
        nmapAdvOutput.put("success", Map.of("type", "boolean", "description", "Scan success flag"));
        nmapAdvOutput.put("error", Map.of("type", "string", "nullable", true, "description", "Error message if any"));
        tools.put("nmap-advanced-scan", new ToolMetadata(
            "nmap-advanced-scan",
            "Performs advanced Nmap scans with many options (ports, OS detection, decoys, etc).",
            nmapAdvInput,
            nmapAdvOutput
        ));
        // Nmap Scan Ports
        Map<String, Object> nmapScanPortsInput = new LinkedHashMap<>();
        nmapScanPortsInput.put("ipAddress", Map.of("type", "string", "description", "Target IP address", "required", true));
        nmapScanPortsInput.put("ports", Map.of("type", "string", "description", "Ports to scan, e.g. '22,80,443'"));
        nmapScanPortsInput.put("scanType", Map.of("type", "string", "description", "Scan type: SYN, UDP, etc."));
        nmapScanPortsInput.put("timing", Map.of("type", "integer", "description", "Timing template (0-5)"));
        Map<String, Object> nmapScanPortsOutput = new LinkedHashMap<>();
        nmapScanPortsOutput.put("result", Map.of("type", "object", "description", "Open ports result map"));
        tools.put("nmap-scan-ports", new ToolMetadata(
            "nmap-scan-ports",
            "Scans specific ports on a single host using Nmap.",
            nmapScanPortsInput,
            nmapScanPortsOutput
        ));
        // Nmap Discover Hosts
        Map<String, Object> nmapDiscoverHostsInput = new LinkedHashMap<>();
        nmapDiscoverHostsInput.put("networkRange", Map.of("type", "string", "description", "CIDR or range to scan", "required", true));
        nmapDiscoverHostsInput.put("timeout", Map.of("type", "integer", "description", "Timeout in seconds"));
        Map<String, Object> nmapDiscoverHostsOutput = new LinkedHashMap<>();
        nmapDiscoverHostsOutput.put("hosts", Map.of("type", "array", "items", Map.of("type", "string"), "description", "List of discovered hosts"));
        tools.put("nmap-discover-hosts", new ToolMetadata(
            "nmap-discover-hosts",
            "Discovers live hosts in a network range using Nmap.",
            nmapDiscoverHostsInput,
            nmapDiscoverHostsOutput
        ));
        // Nmap Resolve Hostname
        Map<String, Object> nmapResolveHostnameInput = new LinkedHashMap<>();
        nmapResolveHostnameInput.put("ipAddress", Map.of("type", "string", "description", "IP address to resolve", "required", true));
        Map<String, Object> nmapResolveHostnameOutput = new LinkedHashMap<>();
        nmapResolveHostnameOutput.put("hostname", Map.of("type", "string", "description", "Resolved hostname"));
        tools.put("nmap-resolve-hostname", new ToolMetadata(
            "nmap-resolve-hostname",
            "Resolves an IP address to a hostname using Nmap.",
            nmapResolveHostnameInput,
            nmapResolveHostnameOutput
        ));
        // Nmap Scan Multiple Hosts
        Map<String, Object> nmapScanMultipleHostsInput = new LinkedHashMap<>();
        nmapScanMultipleHostsInput.put("ipList", Map.of("type", "array", "items", Map.of("type", "string"), "description", "List of IPs to scan", "required", true));
        nmapScanMultipleHostsInput.put("ports", Map.of("type", "string", "description", "Ports to scan, e.g. '80,443'"));
        Map<String, Object> nmapScanMultipleHostsOutput = new LinkedHashMap<>();
        nmapScanMultipleHostsOutput.put("result", Map.of("type", "object", "description", "Map of host to open ports"));
        tools.put("nmap-scan-multiple-hosts", new ToolMetadata(
            "nmap-scan-multiple-hosts",
            "Scans multiple hosts for open ports using Nmap.",
            nmapScanMultipleHostsInput,
            nmapScanMultipleHostsOutput
        ));
        // Nuclei List Templates
        Map<String, Object> nucleiListTemplatesInput = new LinkedHashMap<>();
        Map<String, Object> nucleiListTemplatesOutput = new LinkedHashMap<>();
        nucleiListTemplatesOutput.put("result", Map.of("type", "string", "description", "List of template names (newline separated)"));
        nucleiListTemplatesOutput.put("success", Map.of("type", "boolean", "description", "Success flag"));
        tools.put("nuclei-list-templates", new ToolMetadata(
            "nuclei-list-templates",
            "Lists available Nuclei templates for vulnerability scanning.",
            nucleiListTemplatesInput,
            nucleiListTemplatesOutput
        ));
    }

    /**
     * Returns all registered tool metadata for discovery endpoints.
     * @return Collection of ToolMetadata
     */
    public static Collection<ToolMetadata> getAllTools() {
        return tools.values();
    }

    /**
     * Returns the metadata for a specific tool by name.
     * @param name Tool name
     * @return ToolMetadata or null if not found
     */
    public static ToolMetadata getTool(String name) {
        return tools.get(name);
    }
}
