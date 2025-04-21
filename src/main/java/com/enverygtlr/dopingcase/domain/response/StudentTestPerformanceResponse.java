package com.enverygtlr.dopingcase.domain.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentTestPerformanceResponse(
        UUID testId,
        String title,
        long trueCount,
        long wrongCount,
        long emptyCount
) {
}
