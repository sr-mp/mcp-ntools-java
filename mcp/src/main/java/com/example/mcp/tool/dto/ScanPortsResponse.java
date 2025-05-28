package com.example.mcp.tool.dto;
import java.util.Map;

/**
 * DTO representing the response for a port scan operation using Nmap.
 * <p>
 * Contains a result map with open ports or error information.
 * Why: Standardizes port scan results for MCP protocol clients and agents.
 */
public class ScanPortsResponse {
    /**
     * Map containing scan results (e.g. open ports) or error info.
     */
    private Map<String, Object> result;
    public ScanPortsResponse() {}
    public ScanPortsResponse(Map<String, Object> result) { this.result = result; }
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }
}
