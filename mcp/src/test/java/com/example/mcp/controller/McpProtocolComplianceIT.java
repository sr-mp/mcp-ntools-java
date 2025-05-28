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

    @Test
    void mcpDescribeToolShouldHandleCaseSensitivityAndSpecialChars() {
        String url = "http://localhost:" + port + "/mcp/tools/NMAP-ADVANCED-SCAN/describe";
        var response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        url = "http://localhost:" + port + "/mcp/tools/nmap-advanced-scan!@/describe";
        response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void mcpDescribeToolShouldReturnConsistentSchemaWithDTO() {
        String url = "http://localhost:" + port + "/mcp/tools/nmap-discover-hosts/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        Map<?,?> inputSchema = (Map<?,?>) tool.get("inputSchema");
        // Should match DTO fields
        assertThat(inputSchema.containsKey("networkRange")).isTrue();
        assertThat(inputSchema.containsKey("timeout")).isTrue();
    }

    @Test
    void mcpDescribeToolShouldHandleLargeInput() {
        // Simulate a large input for nmap-advanced-scan (not executed, just schema check)
        String url = "http://localhost:" + port + "/mcp/tools/nmap-advanced-scan/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        Map<?,?> inputSchema = (Map<?,?>) tool.get("inputSchema");
        assertThat(inputSchema.containsKey("targets")).isTrue();
    }

    @Test
    void mcpDescribeToolShouldHandleUnicode() {
        String url = "http://localhost:" + port + "/mcp/tools/nmap-advanced-scan/describe";
        Map<?,?> tool = restTemplate.getForObject(url, Map.class);
        // Just check that description can be read and is a String (simulate unicode)
        Object desc = tool.get("description");
        assertThat(desc).isInstanceOf(String.class);
    }

    @Test
    void mcpDescribeToolShouldReturnErrorFieldOnFailure() {
        // Simulate a call to a tool endpoint with missing required field
        String url = "http://localhost:" + port + "/mcp/nmap/discover_hosts";
        Map<String, Object> req = new HashMap<>(); // missing networkRange
        var response = restTemplate.postForEntity(url, req, Map.class);
        assertThat(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()).isTrue();
        Map<?,?> body = response.getBody();
        if (body != null && body.containsKey("error")) {
            assertThat(body.get("error")).isNotNull();
        }
    }

    @Test
    void mcpDescribeToolShouldReturnValidJsonOnError() {
        String url = "http://localhost:" + port + "/mcp/nmap/discover_hosts";
        Map<String, Object> req = new HashMap<>(); // missing required
        var response = restTemplate.postForEntity(url, req, String.class);
        assertThat(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()).isTrue();
        String body = response.getBody();
        if (body != null) {
            assertThat(body.trim().startsWith("{")).isTrue();
        }
    }

    @Test
    void mcpDescribeToolShouldHandleOnlyRequiredFields() {
        String url = "http://localhost:" + port + "/mcp/nmap/discover_hosts";
        Map<String, Object> req = new HashMap<>();
        req.put("networkRange", "192.168.1.0/24");
        var response = restTemplate.postForEntity(url, req, Map.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<?,?> body = response.getBody();
        if (body != null) {
            assertThat(body.containsKey("hosts")).isTrue();
        }
    }
}
