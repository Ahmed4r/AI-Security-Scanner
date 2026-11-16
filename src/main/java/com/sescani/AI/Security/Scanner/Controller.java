package com.sescani.AI.Security.Scanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    public final OllamaClient client;

    public Controller(OllamaClient client) {
        this.client = client;
    }

    @GetMapping("/test-ai")
    public String testAI() {
        return client.chat("You are a helpful AI assistant.", "Say hello in one sentence.");
    }

}
