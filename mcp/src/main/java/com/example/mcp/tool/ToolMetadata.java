package com.example.mcp.tool;

import java.util.Map;

/**
 * Metadata holder for a tool in the MCP protocol.
 * <p>
 * Stores the tool's name, description, and input/output schemas for agent discovery and validation.
 * <p>
 * Why: Allows the MCP server to describe tools in a machine-readable way for AI agents and clients.
 */
public class ToolMetadata {
    /**
     * Name of the tool (unique identifier).
     */
    private String name;
    /**
     * Human-readable description of the tool's purpose and functionality.
     */
    private String description;
    /**
     * JSON schema describing the tool's input parameters.
     */
    private Map<String, Object> inputSchema;
    /**
     * JSON schema describing the tool's output/result structure.
     */
    private Map<String, Object> outputSchema;

    /**
     * Constructs tool metadata with name, description, and schemas.
     */
    public ToolMetadata(String name, String description, Map<String, Object> inputSchema, Map<String, Object> outputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
    }

    /**
     * @return Tool name
     */
    public String getName() { return name; }
    /**
     * @return Tool description
     */
    public String getDescription() { return description; }
    /**
     * @return Input schema
     */
    public Map<String, Object> getInputSchema() { return inputSchema; }
    /**
     * @return Output schema
     */
    public Map<String, Object> getOutputSchema() { return outputSchema; }
}
