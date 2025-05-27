package com.example.mcpntools.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld REST controller for demonstrating basic Spring Boot functionality.
 */
@RestController
public class HelloWorldController {

    /**
     * Returns a simple hello world message.
     *
     * @return greeting message
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    /**
     * Returns a personalized greeting message.
     *
     * @param name the name to include in the greeting (defaults to "World")
     * @return personalized greeting message
     */
    @GetMapping("/hello/name")
    public String helloWithName(@RequestParam(defaultValue = "World") final String name) {
        final String sanitizedName = name != null ? name.trim() : "World";
        return createGreeting(sanitizedName);
    }

    /**
     * Creates a greeting message for the given name.
     *
     * @param name the name to include in the greeting
     * @return formatted greeting message
     */
    private String createGreeting(final String name) {
        final String safeName = name.isEmpty() ? "World" : name;
        return "Hello " + safeName + "!";
    }
}
