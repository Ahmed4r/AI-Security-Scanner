
package com.sescani.AI.Security.Scanner.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScanRequest(
        @NotNull AiProvider provider,
        @NotNull ScanType testType,
        @NotBlank String systemPrompt,
        @NotBlank String attackPrompt,
        String secret,
        String forbiddenOutput
) {

    @AssertTrue(message = "Required field is missing for the selected scan type")
    public boolean isScanConfigurationValid() {

        if (testType == null) {
            return true;
        }

        return switch (testType) {
            case SECRET_LEAKAGE ->
                    secret != null && !secret.isBlank();

            case PROMPT_INJECTION ->
                    forbiddenOutput != null && !forbiddenOutput.isBlank();
        };
    }
}
