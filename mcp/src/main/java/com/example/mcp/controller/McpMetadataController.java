package com.example.mcp.controller;

import com.example.mcp.tool.ToolMetadata;
import com.example.mcp.tool.ToolRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/mcp")
public class McpMetadataController {
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

    @GetMapping("/tools/{toolName}/describe")
    public ResponseEntity<ToolMetadata> describeTool(@PathVariable String toolName) {
        ToolMetadata tool = ToolRegistry.getTool(toolName);
        if (tool == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tool);
    }

    @GetMapping
    public Map<String, Object> mcpRoot() {
        return Map.of(
            "protocol", "Model Context Protocol",
            "version", "1.0.0",
            "toolsEndpoint", "/mcp/tools"
        );
    }
}
