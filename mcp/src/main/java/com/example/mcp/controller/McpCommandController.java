package com.example.mcp.controller;

import com.example.mcp.tool.NmapService;
import com.example.mcp.tool.NucleiService;
import com.example.mcp.tool.ToolCommandRequest;
import com.example.mcp.tool.ToolCommandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for legacy and generic MCP tool command endpoints.
 * <p>
 * Exposes endpoints for generic Nmap and Nuclei tool execution using a unified request/response format.
 * <p>
 * Why: Provides a flexible interface for tool execution and backward compatibility with older MCP clients.
 */
@RestController
@RequestMapping("/mcp")
public class McpCommandController {
    @Autowired
    private NmapService nmapService;
    @Autowired
    private NucleiService nucleiService;

    /**
     * Executes a host discovery operation using the generic Nmap tool interface.
     * @param req Tool command request (generic format)
     * @return Future with tool command response
     */
    @PostMapping(value = "/nmap/host-discovery", consumes = "application/json", produces = "application/json")
    public CompletableFuture<ResponseEntity<ToolCommandResponse>> nmapHostDiscovery(@RequestBody ToolCommandRequest req) {
        return nmapService.hostDiscovery(req)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * Lists available Nuclei templates using the generic tool interface.
     * @return Future with tool command response
     */
    @GetMapping(value = "/nuclei/templates", produces = "application/json")
    public CompletableFuture<ResponseEntity<ToolCommandResponse>> nucleiTemplates() {
        return nucleiService.listTemplates()
                .thenApply(ResponseEntity::ok);
    }
}
