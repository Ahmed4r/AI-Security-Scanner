
package com.sescani.AI.Security.Scanner;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GroqClient {

    private final RestClient restClient;
    private final String model;
    private final String defaultApiKey;

    public GroqClient(
            @Value("${groq.api-key:}") String defaultApiKey,
            @Value("${groq.model}") String model) {

        this.model = model;
        this.defaultApiKey = defaultApiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    public String chat(
            String systemPrompt,
            String userPrompt,
            String apiKey) {

        String selectedKey =
                apiKey != null && !apiKey.isBlank()
                        ? apiKey
                        : defaultApiKey;

        if (selectedKey == null || selectedKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Groq API key is required");
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", systemPrompt),
                        Map.of(
                                "role", "user",
                                "content", userPrompt)));

        GroqResponse response = restClient.post()
                .uri("/chat/completions")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + selectedKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(GroqResponse.class);

        if (response == null
                || response.choices() == null
                || response.choices().isEmpty()
                || response.choices().get(0).message() == null) {

            throw new IllegalStateException(
                    "Invalid response from Groq API");
        }

        return response.choices().get(0).message().content();
    }

    public record GroqResponse(List<Choice> choices) {}

    public record Choice(Message message) {}

    public record Message(String content) {}
}
