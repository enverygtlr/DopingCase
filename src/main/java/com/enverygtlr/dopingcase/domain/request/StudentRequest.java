package com.enverygtlr.dopingcase.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record StudentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank Long studentNo
) {
}
