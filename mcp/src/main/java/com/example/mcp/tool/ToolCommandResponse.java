package com.example.mcp.tool;

public class ToolCommandResponse {
    private String result;
    private boolean success;

    public ToolCommandResponse() {}
    public ToolCommandResponse(String result, boolean success) {
        this.result = result;
        this.success = success;
    }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
