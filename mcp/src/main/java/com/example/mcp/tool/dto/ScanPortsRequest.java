package com.example.mcp.tool.dto;

/**
 * DTO for a request to scan specific ports on a single host using Nmap.
 * <p>
 * Used by the MCP server to receive the target IP, ports, scan type, and timing options.
 * Why: Enables fine-grained port scanning for agent workflows and automation.
 */
public class ScanPortsRequest {
    /**
     * Target IP address to scan.
     */
    private String ipAddress;
    /**
     * Ports to scan (e.g. "22,80,443"). Optional.
     */
    private String ports;
    /**
     * Scan type (e.g. "SYN", "UDP"). Optional.
     */
    private String scanType;
    /**
     * Timing template (0-5). Optional.
     */
    private Integer timing;

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }
    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }
    public Integer getTiming() { return timing; }
    public void setTiming(Integer timing) { this.timing = timing; }
}
