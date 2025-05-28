package com.example.mcp.tool.dto;

/**
 * DTO for a request to resolve an IP address to a hostname.
 * <p>
 * Used by the MCP server to receive the target IP for DNS resolution.
 * Why: Enables DNS lookups for agent workflows and automation.
 */
public class ResolveHostnameRequest {
    /**
     * The IP address to resolve.
     */
    private String ipAddress;
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
