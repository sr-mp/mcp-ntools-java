package com.example.mcp.controller;

import com.example.mcp.tool.ToolMetadata;
import com.example.mcp.tool.ToolRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * REST controller for MCP protocol metadata endpoints.
 * <p>
 * Exposes endpoints for tool discovery, tool schema description, and protocol version info.
 * <p>
 * Why: Enables AI agents and clients to discover available tools, their descriptions, and schemas
 * in a machine-readable way, as required by the MCP protocol.
 */
@RestController
@RequestMapping("/mcp")
public class McpMetadataController {
    /**
     * Lists all available tools with their names and descriptions for agent discovery.
     * @return List of tool metadata (name, description)
     */
    @GetMapping("/tools")
    public List<Map<String, Object>> listTools() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ToolMetadata tool : ToolRegistry.getAllTools()) {
            result.add(Map.of(
                "name", tool.getName(),
                "description", tool.getDescription()
            ));
        }
        return result;
    }

    /**
     * Returns the metadata and schema for a specific tool.
     * @param toolName The tool's unique name
     * @return ToolMetadata with input/output schemas, or 404 if not found
     */
    @GetMapping("/tools/{toolName}/describe")
    public ResponseEntity<ToolMetadata> describeTool(@PathVariable String toolName) {
        ToolMetadata tool = ToolRegistry.getTool(toolName);
        if (tool == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tool);
    }

    /**
     * Returns MCP protocol version and server info for root discovery.
     * @return Map with protocol, version, and tools endpoint
     */
    @GetMapping
    public Map<String, Object> mcpRoot() {
        return Map.of(
            "protocol", "Model Context Protocol",
            "version", "1.0.0",
            "toolsEndpoint", "/mcp/tools"
        );
    }
}
