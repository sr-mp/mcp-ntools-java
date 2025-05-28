package com.example.mcp.tool;

import java.util.Map;

public class ToolMetadata {
    private String name;
    private String description;
    private Map<String, Object> inputSchema;
    private Map<String, Object> outputSchema;

    public ToolMetadata(String name, String description, Map<String, Object> inputSchema, Map<String, Object> outputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Object> getInputSchema() { return inputSchema; }
    public Map<String, Object> getOutputSchema() { return outputSchema; }
}
