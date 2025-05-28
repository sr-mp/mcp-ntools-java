package com.example.mcp.tool.dto;

/**
 * DTO representing the response for a hostname resolution operation.
 * <p>
 * Contains the resolved hostname for a given IP address, or an error message.
 * Why: Standardizes DNS resolution results for MCP protocol clients and agents.
 */
public class ResolveHostnameResponse {
    /**
     * The resolved hostname, or an error message if resolution failed.
     */
    private String hostname;
    public ResolveHostnameResponse() {}
    public ResolveHostnameResponse(String hostname) { this.hostname = hostname; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
}
