package com.example.softarch.assignment.agent;

public record IterationResult(
        AddIteration iteration,
        String finalDesign,
        String reflection,
        boolean revised) {
}
