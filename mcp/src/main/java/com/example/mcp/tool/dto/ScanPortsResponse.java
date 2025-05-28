package com.example.mcp.tool.dto;
import java.util.Map;

public class ScanPortsResponse {
    private Map<String, Object> result;
    public ScanPortsResponse() {}
    public ScanPortsResponse(Map<String, Object> result) { this.result = result; }
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }
}
