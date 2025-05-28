package com.example.mcp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class McpProtocolComplianceIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void mcpRootEndpointShouldReturnProtocolAndVersion() {
        String url = "http://localhost:" + port + "/mcp";
        Map<?,?> root = restTemplate.getForObject(url, Map.class);
        assertThat(root.get("protocol")).isEqualTo("Model Context Protocol");
        assertThat(root.containsKey("version")).isTrue();
        assertThat(root.containsKey("toolsEndpoint")).isTrue();
    }

    @Test
    void mcpToolsEndpointShouldListAllToolsWithNameAndDescription() {
        String url = "http://localhost:" + port + "/mcp/tools";
        List<?> tools = restTemplate.getForObject(url, List.class);
        assertThat(tools).isNotEmpty();
        for (Object t : tools) {
            Map<?,?> tool = (Map<?,?>) t;
            assertThat(tool.containsKey("name")).isTrue();
            assertThat(tool.containsKey("description")).isTrue();
        }
    }

    @Test
    void mcpDescribeToolEndpointShouldReturnSchemas() {
        String url = "http://localhost:" + port + "/mcp/tools/nmap-advanced-scan/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        assertThat(tool.get("name")).isEqualTo("nmap-advanced-scan");
        assertThat(tool.containsKey("inputSchema")).isTrue();
        assertThat(tool.containsKey("outputSchema")).isTrue();
        Map<?,?> inputSchema = (Map<?,?>) tool.get("inputSchema");
        Map<?,?> outputSchema = (Map<?,?>) tool.get("outputSchema");
        assertThat(inputSchema).isNotEmpty();
        assertThat(outputSchema).isNotEmpty();
    }

    @Test
    void mcpDescribeToolShould404ForUnknownTool() {
        String url = "http://localhost:" + port + "/mcp/tools/does-not-exist/describe";
        var response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void mcpToolsShouldContainAllExpectedTools() {
        String url = "http://localhost:" + port + "/mcp/tools";
        List<?> tools = restTemplate.getForObject(url, List.class);
        Set<String> toolNames = new HashSet<>();
        for (Object t : tools) {
            Map<?,?> tool = (Map<?,?>) t;
            toolNames.add((String) tool.get("name"));
        }
        assertThat(toolNames).contains(
            "nmap-advanced-scan",
            "nmap-scan-ports",
            "nmap-discover-hosts",
            "nmap-resolve-hostname",
            "nmap-scan-multiple-hosts",
            "nuclei-list-templates"
        );
    }

    @Test
    void mcpDescribeToolSchemasShouldBeAccurate() {
        String url = "http://localhost:" + port + "/mcp/tools/nmap-discover-hosts/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        Map<?,?> inputSchema = (Map<?,?>) tool.get("inputSchema");
        Map<?,?> outputSchema = (Map<?,?>) tool.get("outputSchema");
        assertThat(inputSchema.get("networkRange")).isNotNull();
        assertThat(outputSchema.get("hosts")).isNotNull();
    }
}
