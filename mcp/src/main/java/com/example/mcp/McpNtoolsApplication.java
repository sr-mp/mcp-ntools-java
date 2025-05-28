package com.example.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.List;

/**
 * Main entry point for the MCP NTools Spring Boot application.
 * <p>
 * This class bootstraps the application, enables async processing, and sets up component scanning.
 * It also provides a RestTemplate bean for HTTP client operations.
 */
@SpringBootApplication
@EnableAsync
@ComponentScan("com.example.mcp")
public class McpNtoolsApplication {
    /**
     * Starts the Spring Boot application.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(McpNtoolsApplication.class, args);
    }

    /**
     * Provides a RestTemplate bean for HTTP client operations within the app.
     * @return a new RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
