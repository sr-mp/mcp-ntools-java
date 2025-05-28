package com.example.mcp.tool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
public class NmapService {
    /**
     * Performs host discovery (ping scan) using nmap -sn for one or more targets.
     * Accepts a space-separated string of targets (hostnames, IPs, or ranges).
     */
    @Async
    public CompletableFuture<ToolCommandResponse> hostDiscovery(ToolCommandRequest req) {
        String[] targets = req.getTarget().trim().split("\\s+");
        return runNmap(concat(new String[]{"-sn"}, targets));
    }

    /**
     * Utility to concatenate two arrays.
     */
    private static String[] concat(String[] first, String[] second) {
        String[] result = new String[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @Async
    public CompletableFuture<ToolCommandResponse> runNmap(String... args) {
        StringBuilder output = new StringBuilder();
        boolean success = false;
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("nmap");
            for (String arg : args) pb.command().add(arg);
            Process process = pb.start();
            // Timeout logic: wait up to 60 seconds for nmap to finish
            boolean finished = process.waitFor(60, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                output.append("Error: nmap process timed out after 60 seconds\n");
                return CompletableFuture.completedFuture(new ToolCommandResponse(output.toString(), false));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.exitValue();
            success = (exitCode == 0);
        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
        }
        return CompletableFuture.completedFuture(new ToolCommandResponse(output.toString(), success));
    }
}
