package com.example.mcp.controller;

import com.example.mcp.tool.NmapService;
import com.example.mcp.tool.NucleiService;
import com.example.mcp.tool.ToolCommandRequest;
import com.example.mcp.tool.ToolCommandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mcp")
public class McpCommandController {
    @Autowired
    private NmapService nmapService;
    @Autowired
    private NucleiService nucleiService;

    @PostMapping(value = "/nmap/host-discovery", consumes = "application/json", produces = "application/json")
    public CompletableFuture<ResponseEntity<ToolCommandResponse>> nmapHostDiscovery(@RequestBody ToolCommandRequest req) {
        return nmapService.hostDiscovery(req)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/nuclei/templates", produces = "application/json")
    public CompletableFuture<ResponseEntity<ToolCommandResponse>> nucleiTemplates() {
        return nucleiService.listTemplates()
                .thenApply(ResponseEntity::ok);
    }
}
