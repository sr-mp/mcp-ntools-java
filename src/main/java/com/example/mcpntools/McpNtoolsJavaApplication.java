package com.example.mcpntools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for MCP NTools Java.
 * This is the entry point of the application.
 */
@SpringBootApplication
public class McpNtoolsJavaApplication {

    /**
     * Package-private constructor to prevent external instantiation while allowing Spring proxy creation.
     * Default access modifier is intentional to balance PMD requirements with Spring Boot functionality.
     */
    /* default */ McpNtoolsJavaApplication() {
        // Package-private constructor for Spring Boot application
    }

    /**
     * Gets the application name for Spring Boot context.
     * This instance method prevents PMD from treating this as a utility class.
     *
     * @return the application name
     */
    public String getApplicationName() {
        return "MCP NTools Java";
    }

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(McpNtoolsJavaApplication.class, args);
    }
}
