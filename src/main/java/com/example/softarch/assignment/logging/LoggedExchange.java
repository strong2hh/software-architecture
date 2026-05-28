package com.example.softarch.assignment.logging;

import com.example.softarch.assignment.agent.AddIteration;
import java.time.Instant;

public record LoggedExchange(
        Instant timestamp,
        AddIteration iteration,
        String phase,
        String model,
        String systemPrompt,
        String userPrompt,
        String assistantResponse,
        Long promptTokens,
        Long completionTokens,
        Long totalTokens,
        long durationMs) {
}
