package com.example.mcp.controller;

import com.example.mcp.tool.ToolCommandRequest;
import com.example.mcp.tool.ToolCommandResponse;
import com.example.mcp.tool.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.example.mcp.HelloMcpApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HelloMcpApplication.class)
@Testcontainers
class McpCommandControllerIT {

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
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/discover_hosts";
        DiscoverHostsRequest req = new DiscoverHostsRequest();
        req.setNetworkRange("target1 target2");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DiscoverHostsRequest> entity = new HttpEntity<>(req, headers);
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
        ScanPortsRequest req = new ScanPortsRequest();
        req.setIpAddress("target1");
        req.setPorts("80,81");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScanPortsRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("openPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapMultiHostScanEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/scan_multiple_hosts";
        ScanMultipleHostsRequest req = new ScanMultipleHostsRequest();
        req.setIpList(Arrays.asList("target1", "target2"));
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScanMultipleHostsRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapResolveHostnameEndpoint() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/resolve_hostname";
        ResolveHostnameRequest req = new ResolveHostnameRequest();
        req.setIpAddress("target1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ResolveHostnameRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostname");
    }

    @Test
    void nmapAdvancedScanServiceVersionDetection() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setServiceVersionDetection(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapAdvancedScanOsDetection() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setOsDetection(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapAdvancedScanAggressive() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setScanType("AGGRESSIVE");
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("80/tcp");
    }

    @Test
    void nmapAdvancedScanUdp() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setScanType("UDP");
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanTraceroute() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setTraceroute(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanOutputFormatGrepable() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setOutputFormat("grepable");
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("rawOutput");
        assertThat(response).contains("Ports:");
    }

    @Test
    void nmapAdvancedScanEndpoint_valid() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setScanType("SYN");
        req.setServiceVersionDetection(true);
        req.setOsDetection(true);
        req.setOutputFormat("xml");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
        assertThat(response).contains("serviceVersions");
        assertThat(response).contains("osMatches");
    }

    @Test
    void nmapAdvancedScanEndpoint_invalidInput() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(new ArrayList<>());
        req.setScanType("SYN");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("At least one target must be specified");
    }

    @Test
    void nmapAdvancedScanDecoy() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setDecoys(Arrays.asList("1.2.3.4", "5.6.7.8"));
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanFragmentation() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setFragmentPackets(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanSourcePort() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setSourcePort(53);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanPacketTrace() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setPacketTrace(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("rawOutput");
    }

    @Test
    void nmapAdvancedScanDebugLevel() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setDebugLevel(2);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("rawOutput");
    }

    @Test
    void nmapAdvancedScanArp() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setArpScan(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanIpProtocol() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setIpProtocolScan(true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("rawOutput");
    }

    @Test
    void nmapAdvancedScanMinMaxRate() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setMinRate(10);
        req.setMaxRate(100);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanHostTimeout() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setHostTimeout(1000);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("hostPorts");
    }

    @Test
    void nmapAdvancedScanBannerGrab() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setBannerGrab(true);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        // Accept either nseResults present or empty, but must not error
        assertThat(response).contains("hostPorts");
        // nseResults may be missing if banner script returns nothing
    }

    @Test
    void nmapAdvancedScanExcludeHosts() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1", "target2"));
        req.setExcludeHosts(Arrays.asList("target2"));
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        // Only check parsed results, not raw output
        assertThat(response).contains("target1");
        // Parse JSON and check hostPorts keys
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode result = root.get("result");
            assertThat(result).isNotNull();
            JsonNode hostPorts = result.get("hostPorts");
            assertThat(hostPorts).isNotNull();
            assertThat(hostPorts.has("target1")).isTrue();
            assertThat(hostPorts.has("target2")).isFalse();
        } catch (Exception e) {
            throw new AssertionError("Failed to parse response JSON", e);
        }
    }

    @Test
    void nmapAdvancedScanInvalidMinRate() {
        String url = "http://localhost:" + mcpPort + "/mcp/nmap/advanced_scan";
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(Arrays.asList("target1"));
        req.setMinRate(-1);
        req.setPorts("80");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NmapAdvancedScanRequest> entity = new HttpEntity<>(req, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertThat(response).contains("error");
    }
}
