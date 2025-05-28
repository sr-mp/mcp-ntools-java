package com.example.mcp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloMcpController {
    @GetMapping("/mcp/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello MCP, " + name + "!";
    }
}
