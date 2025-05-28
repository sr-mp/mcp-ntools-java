package com.example.mcp.tool.dto;
import java.util.Map;

/**
 * DTO representing the response for an advanced Nmap scan operation.
 * <p>
 * Contains parsed scan results, raw output, success flag, and error message if any.
 * Why: Standardizes advanced scan results for MCP protocol clients and agents.
 */
public class NmapAdvancedScanResponse {
    /**
     * Parsed scan results (e.g. hostPorts, osMatches, etc).
     */
    private Map<String, Object> result;
    /**
     * Raw Nmap output as a string.
     */
    private String rawOutput;
    /**
     * Indicates if the scan was successful.
     */
    private boolean success;
    /**
     * Error message if the scan failed, or null if successful.
     */
    private String error;

    public NmapAdvancedScanResponse() {}
    public NmapAdvancedScanResponse(Map<String, Object> result, String rawOutput, boolean success, String error) {
        this.result = result;
        this.rawOutput = rawOutput;
        this.success = success;
        this.error = error;
    }
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }
    public String getRawOutput() { return rawOutput; }
    public void setRawOutput(String rawOutput) { this.rawOutput = rawOutput; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
