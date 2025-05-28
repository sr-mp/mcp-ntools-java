package com.example.mcp.tool.dto;
import java.util.Optional;

/**
 * DTO for a request to discover live hosts in a network range using Nmap.
 * <p>
 * Used by the MCP server to receive the network range and optional timeout.
 * Why: Enables network discovery for agent workflows and automation.
 */
public class DiscoverHostsRequest {
    /**
     * The network range to scan (CIDR or range).
     */
    private String networkRange;
    /**
     * Timeout in seconds (optional).
     */
    private Integer timeout;

    public String getNetworkRange() { return networkRange; }
    public void setNetworkRange(String networkRange) { this.networkRange = networkRange; }
    public Integer getTimeout() { return timeout; }
    public void setTimeout(Integer timeout) { this.timeout = timeout; }
}
