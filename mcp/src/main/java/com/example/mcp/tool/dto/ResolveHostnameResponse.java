package com.example.mcp.tool.dto;

public class ResolveHostnameResponse {
    private String hostname;
    public ResolveHostnameResponse() {}
    public ResolveHostnameResponse(String hostname) { this.hostname = hostname; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
}
