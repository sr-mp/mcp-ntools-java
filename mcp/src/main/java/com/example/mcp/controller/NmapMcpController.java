package com.example.mcp.controller;

import com.example.mcp.tool.NmapMcpService;
import com.example.mcp.tool.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mcp/nmap")
public class NmapMcpController {
    @Autowired
    private NmapMcpService nmapMcpService;

    @PostMapping("/discover_hosts")
    public ResponseEntity<DiscoverHostsResponse> discoverHosts(@RequestBody DiscoverHostsRequest req) {
        return ResponseEntity.ok(nmapMcpService.discoverHosts(req));
    }

    @PostMapping("/resolve_hostname")
    public ResponseEntity<ResolveHostnameResponse> resolveHostname(@RequestBody ResolveHostnameRequest req) {
        return ResponseEntity.ok(nmapMcpService.resolveHostname(req));
    }

    @PostMapping("/scan_ports")
    public ResponseEntity<ScanPortsResponse> scanPorts(@RequestBody ScanPortsRequest req) {
        return ResponseEntity.ok(nmapMcpService.scanPorts(req));
    }

    @PostMapping("/scan_multiple_hosts")
    public ResponseEntity<ScanMultipleHostsResponse> scanMultipleHosts(@RequestBody ScanMultipleHostsRequest req) {
        return ResponseEntity.ok(nmapMcpService.scanMultipleHosts(req));
    }

    @PostMapping("/advanced_scan")
    public ResponseEntity<NmapAdvancedScanResponse> advancedScan(@RequestBody NmapAdvancedScanRequest req) {
        return ResponseEntity.ok(nmapMcpService.advancedScan(req));
    }
}
