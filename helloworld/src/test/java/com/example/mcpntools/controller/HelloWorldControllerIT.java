package com.example.mcpntools.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class HelloWorldControllerIT {
    @Container
    private static final GenericContainer<?> app = new GenericContainer<>("mcp-ntools-java:latest")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200));

    private static String baseUrl;

    @BeforeAll
    static void setUp() {
        app.start();
        Integer port = app.getMappedPort(8080);
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    static void tearDown() {
        app.stop();
    }

    @Test
    void helloEndpointReturnsHelloWorld() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/hello", String.class);
        assertEquals("Hello World!", response.getBody());
    }

    @Test
    void helloWithNameReturnsPersonalizedGreeting() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/hello/name?name=TestUser", String.class);
        assertEquals("Hello TestUser!", response.getBody());
    }
}
