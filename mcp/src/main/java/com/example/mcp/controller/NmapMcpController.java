package com.example.mcp.controller;

import com.example.mcp.tool.NmapMcpService;
import com.example.mcp.tool.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mcp/nmap")
public class NmapMcpController {
    @Autowired
    private NmapMcpService nmapMcpService;

    @PostMapping(value = "/discover_hosts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> discoverHosts(@RequestBody DiscoverHostsRequest req) {
        if (req.getNetworkRange() == null || req.getNetworkRange().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "'networkRange' is required"));
        }
        return ResponseEntity.ok(nmapMcpService.discoverHosts(req));
    }

    @PostMapping(value = "/resolve_hostname", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResolveHostnameResponse> resolveHostname(@RequestBody ResolveHostnameRequest req) {
        return ResponseEntity.ok(nmapMcpService.resolveHostname(req));
    }

    @PostMapping(value = "/scan_ports", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ScanPortsResponse> scanPorts(@RequestBody ScanPortsRequest req) {
        return ResponseEntity.ok(nmapMcpService.scanPorts(req));
    }

    @PostMapping(value = "/scan_multiple_hosts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ScanMultipleHostsResponse> scanMultipleHosts(@RequestBody ScanMultipleHostsRequest req) {
        return ResponseEntity.ok(nmapMcpService.scanMultipleHosts(req));
    }

    @PostMapping(value = "/advanced_scan", consumes = "application/json", produces = "application/json")
    public ResponseEntity<NmapAdvancedScanResponse> advancedScan(@RequestBody NmapAdvancedScanRequest req) {
        return ResponseEntity.ok(nmapMcpService.advancedScan(req));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("error", ex.getMessage() != null ? ex.getMessage() : "Unknown error"));
    }
}
