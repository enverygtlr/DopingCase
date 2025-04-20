package com.enverygtlr.dopingcase.domain.response;

import java.util.UUID;

public record ChoiceResponse(
        UUID choiceId,
        UUID questionId,
        String content,
        Boolean isCorrectChoice
) {
}
