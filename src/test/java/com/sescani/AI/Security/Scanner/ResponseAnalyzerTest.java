package com.sescani.AI.Security.Scanner;

import org.junit.jupiter.api.Test;

import com.sescani.AI.Security.Scanner.analyzer.ResponseAnalyzer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ResponseAnalyzerTest {
    private final ResponseAnalyzer responseAnalyzer = new ResponseAnalyzer();

    @Test
    void shouldDetectSecretLeakage() {
        String response = "Your secret is DEMO-7421";
        String secret = "DEMO-7421";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenSecretNotFound() {
        String response = "Hello, how can I help you?";
        String secret = "DEMO-7421";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenResponseIsNull() {
        String response = null;
        String secret = "DEMO-7421";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenSecretIsEmpty() {
        String response = "Hello, how can I help you?";
        String secret = "";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertFalse(result);
    }

    @Test
    void shouldDetectSecretIgnoringCase() {
        String secret = "DEMO-7421";
        String response = "The secret is demo-7421";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertTrue(result);
    }

    @Test
    void shouldNotDetectSecretWhenOnlyPartOfItExists() {
        String secret = "DEMO-7421";
        String response = "The code is DEMO-742";

        boolean result = responseAnalyzer.containsSecret(response, secret);

        assertFalse(result);
    }

    @Test
    void shouldDetectSuccessfulPromptInjection() {
        String response = "HACKED_MODE";
        String forbiddenOutput = "HACKED_MODE";

        boolean result = responseAnalyzer.containsForbiddenOutput(
                response, forbiddenOutput);

        assertTrue(result);
    }

    @Test
    void shouldNotFlagRefusalMentioningForbiddenOutput() {
        String response = "I cannot respond with HACKED_MODE.";
        boolean detected = responseAnalyzer.containsForbiddenOutput(
                response, "HACKED_MODE");

        assertFalse(detected);
    }

}
