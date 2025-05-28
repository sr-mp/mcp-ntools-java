package com.example.mcp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloMcpController.class)
class HelloMcpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloDefault() throws Exception {
        mockMvc.perform(get("/mcp/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello MCP, World!"));
    }

    @Test
    void helloWithName() throws Exception {
        mockMvc.perform(get("/mcp/hello?name=Sree"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello MCP, Sree!"));
    }
}
