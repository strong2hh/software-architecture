package com.example.softarch.assignment.agent;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.softarch.assignment.llm.LlmClient;
import com.example.softarch.assignment.llm.LlmRequest;
import com.example.softarch.assignment.llm.LlmResponse;
import com.example.softarch.assignment.logging.ConversationLogger;
import com.example.softarch.assignment.prompt.SystemPromptBuilder;
import com.example.softarch.assignment.report.ReportDraftWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArchitectureDesignAgentTest {

    @TempDir
    Path tempDir;

    @Test
    void runsFourIterationsAndWritesOutputs() throws IOException {
        FakeLlmClient fakeLlmClient = new FakeLlmClient();
        ArchitectureDesignAgent agent = new ArchitectureDesignAgent(
                fakeLlmClient,
                new SystemPromptBuilder(),
                new ConversationLogger(),
                new ReportDraftWriter());

        agent.run(tempDir);

        Path log = tempDir.resolve("conversation-log.jsonl");
        Path addResults = tempDir.resolve("add-results.md");
        Path report = tempDir.resolve("report-draft.md");

        assertThat(log).exists();
        assertThat(addResults).exists();
        assertThat(report).exists();
        assertThat(Files.readAllLines(log)).hasSize(8);
        assertThat(Files.readString(addResults))
                .contains("Iteration 1")
                .contains("Iteration 4")
                .contains("```mermaid");
        assertThat(Files.readString(report))
                .contains("Qwen3-32B")
                .contains("Interaction Cost Analysis");

        assertThat(fakeLlmClient.requests)
                .extracting(LlmRequest::phase)
                .containsExactly(
                        "design", "self_reflection",
                        "design", "self_reflection",
                        "design", "self_reflection",
                        "design", "self_reflection");
    }

    private static class FakeLlmClient implements LlmClient {

        private final List<LlmRequest> requests = new ArrayList<>();

        @Override
        public LlmResponse generate(LlmRequest request) {
            requests.add(request);
            if ("self_reflection".equals(request.phase())) {
                return new LlmResponse("STATUS: PASS\nAll checks passed.", "qwen3-32b", 10L, 5L, 15L, 1L);
            }
            String content = """
                    ## ADD Step 2
                    Goal covered.

                    ## ADD Step 3
                    Element refined.

                    ## ADD Step 4
                    Design concept selected.

                    ## ADD Step 5
                    Responsibilities allocated.

                    ## ADD Step 6
                    ```mermaid
                    flowchart LR
                        A[User] --> B[Hotel Pricing System]
                    ```

                    ## ADD Step 7
                    Current goal achieved.
                    """;
            return new LlmResponse(content, "qwen3-32b", 100L, 50L, 150L, 1L);
        }
    }
}
