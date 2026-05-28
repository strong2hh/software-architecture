package com.example.softarch.assignment.llm;

public record LlmResponse(
        String content,
        String model,
        Long promptTokens,
        Long completionTokens,
        Long totalTokens,
        long durationMs) {
}
