package com.example.mcp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class McpMetadataControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testListTools() {
        String url = "http://localhost:" + port + "/mcp/tools";
        List<?> tools = restTemplate.getForObject(url, List.class);
        assertThat(tools).isNotEmpty();
        boolean foundNmap = tools.stream().anyMatch(t -> t.toString().contains("nmap-advanced-scan"));
        assertThat(foundNmap).isTrue();
    }

    @Test
    void testDescribeTool() {
        String url = "http://localhost:" + port + "/mcp/tools/nmap-advanced-scan/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        assertThat(tool.containsKey("inputSchema")).isTrue();
        assertThat(tool.containsKey("outputSchema")).isTrue();
    }

    @Test
    void testMcpRoot() {
        String url = "http://localhost:" + port + "/mcp";
        Map<?,?> root = restTemplate.getForObject(url, Map.class);
        assertThat(root.get("protocol")).isEqualTo("Model Context Protocol");
        assertThat(root.containsKey("version")).isTrue();
    }
}
