package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentAnswerRequest(
        @NotNull UUID studentId,
        @NotNull UUID testId,
        @NotNull UUID questionId,
        @NotNull UUID choiceId
) {
}