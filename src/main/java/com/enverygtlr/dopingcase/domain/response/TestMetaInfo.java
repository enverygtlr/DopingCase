package com.enverygtlr.dopingcase.domain.response;

import java.util.UUID;

public record TestMetaInfo(
        UUID testId,
        String title
) {
}