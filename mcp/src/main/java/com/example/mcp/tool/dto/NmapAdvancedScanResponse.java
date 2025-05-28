package com.example.mcp.tool.dto;
import java.util.Map;

public class NmapAdvancedScanResponse {
    private Map<String, Object> result;
    private String rawOutput;
    private boolean success;
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
