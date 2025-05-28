package com.example.mcp.controller;

import com.example.mcp.tool.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void testDiscoverHostsEndpoint() {
        String url = "http://localhost:" + port + "/mcp/nmap/discover_hosts";
        DiscoverHostsRequest request = new DiscoverHostsRequest();
        request.setNetworkRange("192.168.1.0/24");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DiscoverHostsRequest> entity = new HttpEntity<>(request, headers);
        
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).isNotNull();
    }

    @Test
    void testScanPortsEndpoint() {
        String url = "http://localhost:" + port + "/mcp/nmap/scan_ports";
        ScanPortsRequest request = new ScanPortsRequest();
        request.setIpAddress("192.168.1.1");
        request.setPorts("80,443");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScanPortsRequest> entity = new HttpEntity<>(request, headers);
        
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).isNotNull();
    }
}
