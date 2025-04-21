package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TestAttendanceRequest(
        @NotNull UUID studentId,
        @NotNull UUID testId
) {
}