package com.example.softarch.assignment.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ModelConfigurationTest {

    @Test
    void configuresQwen32bForDashScope() throws IOException {
        String yaml = Files.readString(Path.of("src/main/resources/application.yml"));

        assertThat(yaml)
                .contains("dashscope")
                .contains("AI_DASHSCOPE_API_KEY")
                .contains("DASHSCOPE_API_KEY")
                .contains("model: qwen3-32b")
                .contains("read-timeout: 180000")
                .contains("enable-thinking: false")
                .contains("web-application-type: none");
    }
}
