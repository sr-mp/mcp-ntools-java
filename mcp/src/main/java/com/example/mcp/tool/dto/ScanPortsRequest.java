package com.example.mcp.tool.dto;

public class ScanPortsRequest {
    private String ipAddress;
    private String ports; // e.g. "22,80,443" or null
    private String scanType; // e.g. "SYN", "UDP", etc.
    private Integer timing; // 0-5, optional
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }
    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }
    public Integer getTiming() { return timing; }
    public void setTiming(Integer timing) { this.timing = timing; }
}
