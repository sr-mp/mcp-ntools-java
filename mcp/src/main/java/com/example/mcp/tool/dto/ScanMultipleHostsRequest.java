package com.example.mcp.tool.dto;
import java.util.List;

public class ScanMultipleHostsRequest {
    private List<String> ipList;
    private String ports;
    public List<String> getIpList() { return ipList; }
    public void setIpList(List<String> ipList) { this.ipList = ipList; }
    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }
}
