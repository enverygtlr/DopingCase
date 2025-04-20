package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record QuestionRequest(
        @NotBlank String content,
        @NotEmpty List<ChoiceRequest> choices
) {
}
