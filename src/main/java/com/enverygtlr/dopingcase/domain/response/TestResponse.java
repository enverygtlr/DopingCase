package com.enverygtlr.dopingcase.domain.response;

import java.util.List;
import java.util.UUID;

public record TestResponse (
        UUID testId,
        String title,
        List<QuestionResponse> questions
) {
}
