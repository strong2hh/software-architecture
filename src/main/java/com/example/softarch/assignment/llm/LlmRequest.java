package com.example.softarch.assignment.llm;

import com.example.softarch.assignment.agent.AddIteration;

public record LlmRequest(
        AddIteration iteration,
        String phase,
        String systemPrompt,
        String userPrompt) {
}
