package com.example.softarch.assignment.logging;

import com.example.softarch.assignment.agent.AddIteration;
import com.example.softarch.assignment.llm.LlmResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ConversationLogger {

    private final ObjectMapper objectMapper;

    public ConversationLogger() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public LoggedExchange log(
            Path logPath,
            AddIteration iteration,
            String phase,
            String systemPrompt,
            String userPrompt,
            LlmResponse response) throws IOException {
        LoggedExchange exchange = new LoggedExchange(
                Instant.now(),
                iteration,
                phase,
                response.model(),
                systemPrompt,
                userPrompt,
                response.content(),
                response.promptTokens(),
                response.completionTokens(),
                response.totalTokens(),
                response.durationMs());
        Files.writeString(
                logPath,
                objectMapper.writeValueAsString(exchange) + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        return exchange;
    }
}
