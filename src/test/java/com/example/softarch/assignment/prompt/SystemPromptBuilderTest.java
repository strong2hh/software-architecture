package com.example.softarch.assignment.prompt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SystemPromptBuilderTest {

    @Test
    void buildsPromptWithPriorKnowledgeAndAssignmentRules() {
        String prompt = new SystemPromptBuilder().build();

        assertThat(prompt)
                .contains("Attribute-Driven Design")
                .contains("Hotel Pricing System")
                .contains("Iteration 1: Establishing an Overall System Structure")
                .contains("Iteration 4: Addressing Development and Operations")
                .contains("Do not use few-shot examples")
                .contains("Mermaid or PlantUML")
                .contains("HPS-2: Change Prices")
                .contains("QA-1");
    }
}
