package com.example.mcp.tool;

/**
 * Generic DTO for a tool command request in the MCP protocol.
 * <p>
 * Used for legacy or generic tool execution endpoints.
 * Why: Enables flexible tool invocation and extensibility for new tool types.
 */
public class ToolCommandRequest {
    /**
     * The target for the tool command (e.g. host, IP, etc).
     */
    private String target;
    // Add more fields as needed for extensibility

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
}
