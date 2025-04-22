package com.enverygtlr.dopingcase.exception.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        String title,
        String message,
        int httpCode
) {}