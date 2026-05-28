package com.example.softarch.assignment.prompt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class SystemPromptBuilder {

    public String build() {
        String instruction = read("prompts/system-instruction.md");
        String add = read("prior-knowledge/add-3.md");
        String hps = read("prior-knowledge/hotel-pricing-system.md");

        return """
                %s

                # Prior Knowledge: ADD 3.0

                %s

                # Prior Knowledge: Hotel Pricing System

                %s

                # Fixed Iteration Plan

                Iteration 1: Establishing an Overall System Structure
                Iteration 2: Identifying Structures to Support Primary Functionality
                Iteration 3: Addressing Reliability and Availability Quality Attributes
                Iteration 4: Addressing Development and Operations
                """.formatted(instruction, add, hps);
    }

    private String read(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read classpath resource: " + path, e);
        }
    }
}
