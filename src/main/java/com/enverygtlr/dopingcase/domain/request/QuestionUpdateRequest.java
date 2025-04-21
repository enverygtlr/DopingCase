package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record QuestionUpdateRequest(
        @NotBlank UUID questionId,
        @NotBlank UUID testId,
        @NotBlank String content,
        @NotEmpty List<ChoiceRequest> choices
) {
}