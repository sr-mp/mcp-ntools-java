package com.example.mcp.tool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;
import java.util.List;

/**
 * Service class for Nuclei tool integration in the MCP protocol.
 * <p>
 * Provides asynchronous methods to interact with the Nuclei vulnerability scanner.
 * <p>
 * Why: Allows the MCP server to expose Nuclei-based security scanning and template listing to agents/clients.
 */
@Service
public class NucleiService {
    /**
     * Asynchronously lists available Nuclei templates for vulnerability scanning.
     * For MVP, returns a dummy list. Replace with real nuclei call for production.
     *
     * @return Future with tool command response containing template names
     */
    @Async
    public CompletableFuture<ToolCommandResponse> listTemplates() {
        // For MVP, return a dummy list. Replace with real nuclei call later.
        List<String> templates = Arrays.asList("cves/2021/CVE-2021-1234.yaml", "misconfiguration/ssh.yaml");
        String result = String.join("\n", templates);
        return CompletableFuture.completedFuture(new ToolCommandResponse(result, true));
    }
}
