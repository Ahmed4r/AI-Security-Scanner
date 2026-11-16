package com.sescani.AI.Security.Scanner;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OllamaClient {

    private final RestClient restClient;
    private final String model;

    public OllamaClient(
            RestClient.Builder builder,
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model}") String model
    ) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();

        this.model = model;
    }

    public String chat(String systemPrompt, String userPrompt) {

        Map<String, Object> body = Map.of(
                "model", model,
                "stream", false,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        OllamaResponse response = restClient.post()
                .uri("/api/chat")
                .body(body)
                .retrieve()
                .body(OllamaResponse.class);

        if (response == null || response.message() == null) {
            throw new IllegalStateException("Invalid Ollama response");
        }

        return response.message().content();
    }

    public record OllamaResponse(Message message) {}

    public record Message(String content) {}
}