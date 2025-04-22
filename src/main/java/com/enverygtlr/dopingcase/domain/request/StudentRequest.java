package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StudentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank Long studentNo
) {
}
