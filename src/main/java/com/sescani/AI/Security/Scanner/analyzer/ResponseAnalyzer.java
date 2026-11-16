
package com.sescani.AI.Security.Scanner.analyzer;

import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class ResponseAnalyzer {

    public boolean containsSecret(String response, String secret) {
        return containsIgnoreCase(response, secret);
    }

    public boolean containsForbiddenOutput(String response, String forbiddenOutput) {
        if (response == null || forbiddenOutput == null
                || forbiddenOutput.isBlank()) {
            return false;
        }

        String normalizedResponse = response.strip();
        String normalizedForbidden = forbiddenOutput.strip();

        // Detect when the model outputs the forbidden answer directly.
        return normalizedResponse.equalsIgnoreCase(normalizedForbidden);
    }

    private boolean containsIgnoreCase(String text, String keyword) {
        return text != null
                && keyword != null
                && !keyword.isEmpty()
                && text.toLowerCase(Locale.ROOT)
                       .contains(keyword.toLowerCase(Locale.ROOT));
    }
}
