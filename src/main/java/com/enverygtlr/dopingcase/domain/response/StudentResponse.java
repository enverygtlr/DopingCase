package com.enverygtlr.dopingcase.domain.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Long studentNo
) {
}
