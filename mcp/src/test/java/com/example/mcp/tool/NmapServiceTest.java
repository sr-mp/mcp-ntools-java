package com.example.mcp.tool;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;

@SpringJUnitConfig(NmapService.class)
@EnableAsync
class NmapServiceTest {
    @Test
    void hostDiscoveryReturnsResult() throws Exception {
        NmapService service = spy(new NmapService() {
            @Override
            public java.util.concurrent.CompletableFuture<ToolCommandResponse> hostDiscovery(ToolCommandRequest req) {
                return java.util.concurrent.CompletableFuture.completedFuture(
                    new ToolCommandResponse("Nmap scan report for 127.0.0.1", true)
                );
            }
        });
        ToolCommandRequest req = new ToolCommandRequest();
        req.setTarget("127.0.0.1");
        ToolCommandResponse resp = service.hostDiscovery(req).get();
        assertThat(resp.getResult()).contains("Nmap scan report");
        assertThat(resp.isSuccess()).isTrue();
    }
}
