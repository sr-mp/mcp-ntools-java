package com.example.mcp.controller;

import com.example.mcp.tool.ToolCommandRequest;
import com.example.mcp.tool.ToolCommandResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class McpCommandControllerIT {
    @TestConfiguration
    static class RestTemplateConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    static Network testNetwork;
    static GenericContainer<?> targetContainer;
    static GenericContainer<?> secondTarget;
    static GenericContainer<?> mcpContainer;
    static int mcpPort;

    @BeforeAll
    static void setUp() {
        testNetwork = Network.newNetwork();
        targetContainer = new GenericContainer<>(DockerImageName.parse("nginx:alpine"))
            .withExposedPorts(80)
            .withNetwork(testNetwork)
            .withNetworkAliases("target1")
            .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());
        targetContainer.start();
        secondTarget = new GenericContainer<>(DockerImageName.parse("nginx:alpine"))
            .withExposedPorts(80)
            .withNetwork(testNetwork)
            .withNetworkAliases("target2")
            .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());
        secondTarget.start();
        mcpContainer = new GenericContainer<>(DockerImageName.parse("mcp-ntools-java:latest"))
            .withExposedPorts(8080)
            .withNetwork(testNetwork)
            .withNetworkAliases("mcp")
            .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());
        mcpContainer.start();
        mcpPort = mcpContainer.getMappedPort(8080);
    }

    @AfterAll
    static void tearDown() {
        if (mcpContainer != null) mcpContainer.stop();
        if (targetContainer != null) targetContainer.stop();
        if (secondTarget != null) secondTarget.stop();
        if (testNetwork != null) testNetwork.close();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void nmapHostDiscoveryEndpoint() {
        String networkRange = "target1 target2";
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/discover_hosts";
        String body = String.format("{\"networkRange\":\"%s\"}", networkRange);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("target1");
        assertThat(response).contains("target2");
    }

    @Test
    void nucleiTemplatesEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nuclei/templates";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("cves/2021/CVE-2021-1234.yaml");
    }

    @Test
    void nmapPortScanEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/scan_ports";
        String body = "{\"ipAddress\":\"target1\",\"ports\":\"80,81\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("openPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapMultiHostScanEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/scan_multiple_hosts";
        String body = "{\"ipList\":[\"target1\",\"target2\"],\"ports\":\"80\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapResolveHostnameEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/resolve_hostname";
        String body = "{\"ipAddress\":\"target1\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostname");
    }
}
