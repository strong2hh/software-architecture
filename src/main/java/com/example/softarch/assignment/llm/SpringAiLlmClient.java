package com.example.softarch.assignment.llm;

import java.lang.reflect.Method;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class SpringAiLlmClient implements LlmClient {

    private final ChatClient.Builder chatClientBuilder;
    private final String model;

    public SpringAiLlmClient(
            ChatClient.Builder chatClientBuilder,
            @Value("${spring.ai.dashscope.chat.options.model:qwen3-32b}") String model) {
        this.chatClientBuilder = chatClientBuilder;
        this.model = model;
    }

    @Override
    public LlmResponse generate(LlmRequest request) {
        long started = System.nanoTime();
        ChatResponse response = chatClientBuilder.build()
                .prompt()
                .system(request.systemPrompt())
                .user(request.userPrompt())
                .call()
                .chatResponse();

        String content = response.getResult().getOutput().getText();
        long durationMs = (System.nanoTime() - started) / 1_000_000;
        TokenUsage usage = TokenUsage.from(response);
        return new LlmResponse(content, model, usage.promptTokens(), usage.completionTokens(), usage.totalTokens(), durationMs);
    }

    private record TokenUsage(Long promptTokens, Long completionTokens, Long totalTokens) {

        static TokenUsage from(ChatResponse response) {
            try {
                Object metadata = response.getMetadata();
                Object usage = invoke(metadata, "getUsage");
                Long prompt = asLong(invoke(usage, "getPromptTokens"));
                Long completion = firstNonNull(
                        asLong(invoke(usage, "getCompletionTokens")),
                        asLong(invoke(usage, "getGenerationTokens")));
                Long total = asLong(invoke(usage, "getTotalTokens"));
                return new TokenUsage(prompt, completion, total);
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                return new TokenUsage(null, null, null);
            }
        }

        private static Object invoke(Object target, String methodName) throws ReflectiveOperationException {
            if (target == null) {
                return null;
            }
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        }

        private static Long asLong(Object value) {
            if (value instanceof Number number) {
                return number.longValue();
            }
            return null;
        }

        private static Long firstNonNull(Long first, Long second) {
            return first != null ? first : second;
        }
    }
}
