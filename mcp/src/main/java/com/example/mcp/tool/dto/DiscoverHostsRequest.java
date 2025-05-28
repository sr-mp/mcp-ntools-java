package com.example.mcp.tool.dto;
import java.util.Optional;

public class DiscoverHostsRequest {
    private String networkRange;
    private Integer timeout; // seconds, optional
    public String getNetworkRange() { return networkRange; }
    public void setNetworkRange(String networkRange) { this.networkRange = networkRange; }
    public Integer getTimeout() { return timeout; }
    public void setTimeout(Integer timeout) { this.timeout = timeout; }
}
