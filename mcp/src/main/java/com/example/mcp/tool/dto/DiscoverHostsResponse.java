package com.example.mcp.tool.dto;
import java.util.List;

public class DiscoverHostsResponse {
    private List<String> hosts;
    public DiscoverHostsResponse() {}
    public DiscoverHostsResponse(List<String> hosts) { this.hosts = hosts; }
    public List<String> getHosts() { return hosts; }
    public void setHosts(List<String> hosts) { this.hosts = hosts; }
}
