package com.example.mcp.tool.dto;
import java.util.Map;

public class ScanMultipleHostsResponse {
    private Map<String, Object> result;
    public ScanMultipleHostsResponse() {}
    public ScanMultipleHostsResponse(Map<String, Object> result) { this.result = result; }
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }
}
