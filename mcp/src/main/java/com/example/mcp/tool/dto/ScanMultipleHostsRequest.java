package com.example.mcp.tool.dto;
import java.util.List;

/**
 * DTO for a request to scan multiple hosts for open ports using Nmap.
 * <p>
 * Used by the MCP server to receive a list of IPs and optional ports to scan.
 * Why: Enables batch port scanning for agent workflows and automation.
 */
public class ScanMultipleHostsRequest {
    /**
     * List of IP addresses to scan.
     */
    private List<String> ipList;
    /**
     * Ports to scan (e.g. "80,443"). Optional.
     */
    private String ports;
    public List<String> getIpList() { return ipList; }
    public void setIpList(List<String> ipList) { this.ipList = ipList; }
    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }
}
