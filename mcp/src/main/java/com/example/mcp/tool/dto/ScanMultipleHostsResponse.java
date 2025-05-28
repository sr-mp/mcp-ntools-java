package com.example.mcp.tool.dto;
import java.util.Map;

/**
 * DTO representing the response for a multi-host port scan operation using Nmap.
 * <p>
 * Contains a result map with open ports per host or error information.
 * Why: Standardizes multi-host scan results for MCP protocol clients and agents.
 */
public class ScanMultipleHostsResponse {
    /**
     * Map containing scan results (e.g. open ports per host) or error info.
     */
    private Map<String, Object> result;
    public ScanMultipleHostsResponse() {}
    public ScanMultipleHostsResponse(Map<String, Object> result) { this.result = result; }
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }
}
