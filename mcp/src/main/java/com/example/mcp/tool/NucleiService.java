package com.example.mcp.tool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;
import java.util.List;

@Service
public class NucleiService {
    @Async
    public CompletableFuture<ToolCommandResponse> listTemplates() {
        // For MVP, return a dummy list. Replace with real nuclei call later.
        List<String> templates = Arrays.asList("cves/2021/CVE-2021-1234.yaml", "misconfiguration/ssh.yaml");
        String result = String.join("\n", templates);
        return CompletableFuture.completedFuture(new ToolCommandResponse(result, true));
    }
}
