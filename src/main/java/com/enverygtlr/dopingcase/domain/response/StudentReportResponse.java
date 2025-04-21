package com.enverygtlr.dopingcase.domain.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record StudentReportResponse(
        UUID studentId,
        String fullName,
        String email,
        Long studentNo,
        List<StudentTestPerformanceResponse> performances
) {
}
