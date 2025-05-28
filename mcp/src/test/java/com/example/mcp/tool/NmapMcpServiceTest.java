package com.example.mcp.tool;

import com.example.mcp.tool.dto.NmapAdvancedScanRequest;
import com.example.mcp.tool.dto.NmapAdvancedScanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class NmapMcpServiceTest {
    @Test
    void advancedScanRejectsMissingTargets() {
        NmapMcpService service = new NmapMcpService();
        NmapService mockNmapService = Mockito.mock(NmapService.class);
        service.setNmapService(mockNmapService);
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        NmapAdvancedScanResponse resp = service.advancedScan(req);
        assertThat(resp.isSuccess()).isFalse();
        assertThat(resp.getError()).contains("target");
    }

    @Test
    void advancedScanRejectsInvalidTiming() {
        NmapMcpService service = new NmapMcpService();
        NmapService mockNmapService = Mockito.mock(NmapService.class);
        service.setNmapService(mockNmapService);
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(List.of("127.0.0.1"));
        req.setTiming(10);
        NmapAdvancedScanResponse resp = service.advancedScan(req);
        assertThat(resp.isSuccess()).isFalse();
        assertThat(resp.getError()).contains("Timing");
    }

    @Test
    void advancedScanBuildsDecoyArgs() {
        NmapMcpService service = new NmapMcpService();
        NmapService mockNmapService = Mockito.mock(NmapService.class);
        service.setNmapService(mockNmapService);
        NmapAdvancedScanRequest req = new NmapAdvancedScanRequest();
        req.setTargets(List.of("127.0.0.1"));
        req.setDecoys(List.of("1.2.3.4","5.6.7.8"));
        Mockito.doReturn(java.util.concurrent.CompletableFuture.completedFuture(
            new ToolCommandResponse("<?xml version=\"1.0\"?><nmaprun></nmaprun>", true)
        )).when(mockNmapService).runNmap(Mockito.any(String[].class));
        NmapAdvancedScanResponse resp = service.advancedScan(req);
        if (!resp.isSuccess()) {
            System.out.println("DEBUG FAILURE: error=" + resp.getError());
            fail("Expected success but got error: " + resp.getError());
        }
        assertThat(resp.getError()).isNull();
    }
}
