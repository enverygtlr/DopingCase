package com.enverygtlr.dopingcase.domain.response;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID questionId,
        UUID testId,
        String content,
        List<ChoiceResponse> choices
) {
}
