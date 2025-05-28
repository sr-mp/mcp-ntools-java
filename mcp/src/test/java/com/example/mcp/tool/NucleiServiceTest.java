package com.example.mcp.tool;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(NucleiService.class)
@EnableAsync
class NucleiServiceTest {
    @Test
    void listTemplatesReturnsDummyList() throws ExecutionException, InterruptedException {
        NucleiService service = new NucleiService();
        ToolCommandResponse resp = service.listTemplates().get();
        assertThat(resp.getResult()).contains("cves/2021/CVE-2021-1234.yaml");
        assertThat(resp.isSuccess()).isTrue();
    }
}
