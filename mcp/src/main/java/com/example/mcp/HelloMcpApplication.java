package com.example.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HelloMcpApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloMcpApplication.class, args);
    }
}
