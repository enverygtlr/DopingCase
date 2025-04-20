package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChoiceRequest(
        @NotBlank String content,
        @NotNull Boolean isCorrectChoice
) {
}