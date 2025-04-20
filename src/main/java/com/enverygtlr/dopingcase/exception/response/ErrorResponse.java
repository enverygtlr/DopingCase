package com.enverygtlr.dopingcase.exception.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse (
        @NotNull LocalDateTime timestamp,
        @NotNull String message,
        @NotNull int httpCode
) {
}
