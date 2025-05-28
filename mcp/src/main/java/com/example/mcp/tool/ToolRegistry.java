package com.example.mcp.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Registry for all available tools in the MCP server.
 * <p>
 * Loads tool metadata from a JSON file in resources for dynamic discovery and schema description.
 */
@SuppressWarnings("unchecked")
public class ToolRegistry {
    private static final Map<String, ToolMetadata> tools = new LinkedHashMap<>();

    static {
        try (InputStream is = ToolRegistry.class.getClassLoader().getResourceAsStream("tool-registry.json")) {
            if (is == null) throw new RuntimeException("tool-registry.json not found in resources");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> raw = mapper.readValue(is, Map.class);
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                Map<String, Object> meta = (Map<String, Object>) entry.getValue();
                String name = (String) meta.get("name");
                String description = (String) meta.get("description");
                Map<String, Object> inputSchema = meta.get("inputSchema") instanceof Map ? (Map<String, Object>) meta.get("inputSchema") : new LinkedHashMap<>();
                Map<String, Object> outputSchema = meta.get("outputSchema") instanceof Map ? (Map<String, Object>) meta.get("outputSchema") : new LinkedHashMap<>();
                tools.put(entry.getKey(), new ToolMetadata(name, description, inputSchema, outputSchema));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tool registry", e);
        }
    }

    /**
     * Returns all registered tool metadata for discovery endpoints.
     * @return Collection of ToolMetadata
     */
    public static Collection<ToolMetadata> getAllTools() {
        return tools.values();
    }

    /**
     * Returns the metadata for a specific tool by name.
     * @param name Tool name
     * @return ToolMetadata or null if not found
     */
    public static ToolMetadata getTool(String name) {
        return tools.get(name);
    }
}
