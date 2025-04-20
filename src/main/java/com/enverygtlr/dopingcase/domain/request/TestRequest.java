package com.enverygtlr.dopingcase.domain.request;

import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TestRequest(
        @NotBlank String title,
        @NotEmpty List<QuestionRequest> questions
) {
}