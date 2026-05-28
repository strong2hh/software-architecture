package com.example.softarch.assignment.report;

import com.example.softarch.assignment.agent.IterationResult;
import com.example.softarch.assignment.logging.LoggedExchange;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReportDraftWriter {

    public String write(List<IterationResult> results, List<LoggedExchange> exchanges) {
        long knownTokens = exchanges.stream()
                .map(LoggedExchange::totalTokens)
                .filter(tokens -> tokens != null)
                .mapToLong(Long::longValue)
                .sum();
        boolean hasUnknownTokens = exchanges.stream().anyMatch(exchange -> exchange.totalTokens() == null);
        double minutes = exchanges.stream().mapToLong(LoggedExchange::durationMs).sum() / 60_000.0;

        StringBuilder report = new StringBuilder("# Report Draft\n\n");
        report.append("## 1. Output Results of ADD\n");
        for (IterationResult result : results) {
            report.append("\n### Iteration ")
                    .append(result.iteration().number())
                    .append(": ")
                    .append(result.iteration().goal())
                    .append("\n\n")
                    .append(result.finalDesign())
                    .append("\n");
        }

        report.append("\n## 2. Interaction Cost Analysis\n\n")
                .append("| Item | Value |\n")
                .append("| --- | --- |\n")
                .append("| The way of completing the assignment | Single-agent, sequential reasoning + self-reflection |\n")
                .append("| The LLM used | Qwen3-32B |\n")
                .append("| Number of Human Interactions (turns) | 1 human run command; ")
                .append(exchanges.size())
                .append(" LLM API calls |\n")
                .append("| Token Consumption (K tokens) | ")
                .append(hasUnknownTokens ? "Partially unavailable from API metadata; known total: " : "")
                .append(String.format("%.2f", knownTokens / 1000.0))
                .append(" |\n")
                .append("| Time Cost (min) | ")
                .append(String.format("%.2f", minutes))
                .append(" |\n");

        report.append("\n## 3. Individual Reflection\n\n")
                .append("### Problems Encountered and Solutions Adopted\n\n")
                .append("To be completed by the group after reviewing the generated ADD results and the conversation log.\n\n")
                .append("### Personal Contributions\n\n")
                .append("| Name (Chinese) | Contributions |\n")
                .append("| --- | --- |\n")
                .append("|  |  |\n");

        return report.toString();
    }
}
