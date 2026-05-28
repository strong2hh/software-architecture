package com.example.softarch.assignment.agent;

import com.example.softarch.assignment.llm.LlmClient;
import com.example.softarch.assignment.llm.LlmRequest;
import com.example.softarch.assignment.llm.LlmResponse;
import com.example.softarch.assignment.logging.ConversationLogger;
import com.example.softarch.assignment.logging.LoggedExchange;
import com.example.softarch.assignment.prompt.SystemPromptBuilder;
import com.example.softarch.assignment.report.ReportDraftWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArchitectureDesignAgent {

    private final LlmClient llmClient;
    private final SystemPromptBuilder systemPromptBuilder;
    private final ConversationLogger conversationLogger;
    private final ReportDraftWriter reportDraftWriter;

    public ArchitectureDesignAgent(
            LlmClient llmClient,
            SystemPromptBuilder systemPromptBuilder,
            ConversationLogger conversationLogger,
            ReportDraftWriter reportDraftWriter) {
        this.llmClient = llmClient;
        this.systemPromptBuilder = systemPromptBuilder;
        this.conversationLogger = conversationLogger;
        this.reportDraftWriter = reportDraftWriter;
    }

    public void run(Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Path logPath = outputDir.resolve("conversation-log.jsonl");
        Files.deleteIfExists(logPath);

        String systemPrompt = systemPromptBuilder.build();
        List<IterationResult> results = new ArrayList<>();
        List<LoggedExchange> exchanges = new ArrayList<>();
        StringBuilder priorOutputs = new StringBuilder();

        for (AddIteration iteration : AddIteration.values()) {
            LlmResponse designResponse = llmClient.generate(new LlmRequest(
                    iteration,
                    "design",
                    systemPrompt,
                    buildDesignPrompt(iteration, priorOutputs.toString())));
            exchanges.add(conversationLogger.log(logPath, iteration, "design", systemPrompt,
                    buildDesignPrompt(iteration, priorOutputs.toString()), designResponse));

            LlmResponse reflectionResponse = llmClient.generate(new LlmRequest(
                    iteration,
                    "self_reflection",
                    systemPrompt,
                    buildReflectionPrompt(iteration, designResponse.content())));
            exchanges.add(conversationLogger.log(logPath, iteration, "self_reflection", systemPrompt,
                    buildReflectionPrompt(iteration, designResponse.content()), reflectionResponse));

            String finalDesign = designResponse.content();
            boolean revised = needsRevision(reflectionResponse.content());
            if (revised) {
                LlmResponse revisionResponse = llmClient.generate(new LlmRequest(
                        iteration,
                        "revision",
                        systemPrompt,
                        buildRevisionPrompt(iteration, designResponse.content(), reflectionResponse.content())));
                exchanges.add(conversationLogger.log(logPath, iteration, "revision", systemPrompt,
                        buildRevisionPrompt(iteration, designResponse.content(), reflectionResponse.content()), revisionResponse));
                finalDesign = revisionResponse.content();
            }

            IterationResult result = new IterationResult(iteration, finalDesign, reflectionResponse.content(), revised);
            results.add(result);
            appendPriorOutput(priorOutputs, result);
        }

        Files.writeString(outputDir.resolve("add-results.md"), buildAddResults(results), StandardCharsets.UTF_8);
        Files.writeString(outputDir.resolve("report-draft.md"), reportDraftWriter.write(results, exchanges), StandardCharsets.UTF_8);
    }

    private String buildDesignPrompt(AddIteration iteration, String priorOutputs) {
        String steps = String.join(", ", iteration.requiredStepLabels());
        return """
                Complete iteration %d: %s.

                Required ADD sections for this iteration: %s.

                Produce:
                - The required ADD step outputs.
                - Significant design decisions and rationale.
                - At least one Mermaid or PlantUML view.
                - Traceability from decisions to the provided use cases, quality attributes, concerns, or constraints.

                Prior iteration outputs:
                %s
                """.formatted(iteration.number(), iteration.goal(), steps, priorOutputs.isBlank() ? "None." : priorOutputs);
    }

    private String buildReflectionPrompt(AddIteration iteration, String designOutput) {
        String steps = String.join(", ", iteration.requiredStepLabels());
        return """
                Self-reflect on the following output for iteration %d: %s.

                Required ADD sections: %s.

                Check only against the provided prior knowledge and system rules. Return the first line exactly as STATUS: PASS or STATUS: REVISE.

                Output to review:
                %s
                """.formatted(iteration.number(), iteration.goal(), steps, designOutput);
    }

    private String buildRevisionPrompt(AddIteration iteration, String designOutput, String reflectionOutput) {
        return """
                Revise the output for iteration %d: %s using only the self-reflection findings below.
                Keep the same assignment constraints. Include the required ADD sections and Mermaid or PlantUML views.

                Original output:
                %s

                Self-reflection findings:
                %s
                """.formatted(iteration.number(), iteration.goal(), designOutput, reflectionOutput);
    }

    private boolean needsRevision(String reflectionContent) {
        return reflectionContent == null || !reflectionContent.stripLeading().startsWith("STATUS: PASS");
    }

    private void appendPriorOutput(StringBuilder priorOutputs, IterationResult result) {
        priorOutputs.append("\n\n## Iteration ")
                .append(result.iteration().number())
                .append(": ")
                .append(result.iteration().goal())
                .append("\n")
                .append(result.finalDesign());
    }

    private String buildAddResults(List<IterationResult> results) {
        StringBuilder markdown = new StringBuilder("# Output Results of ADD\n");
        for (IterationResult result : results) {
            markdown.append("\n## Iteration ")
                    .append(result.iteration().number())
                    .append(": ")
                    .append(result.iteration().goal())
                    .append("\n\n")
                    .append(result.finalDesign())
                    .append("\n\n### Self-reflection\n\n")
                    .append(result.reflection())
                    .append("\n");
            if (result.revised()) {
                markdown.append("\nRevision applied: yes\n");
            }
        }
        return markdown.toString();
    }
}
