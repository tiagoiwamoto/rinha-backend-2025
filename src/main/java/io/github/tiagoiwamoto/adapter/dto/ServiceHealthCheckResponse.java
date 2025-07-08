package io.github.tiagoiwamoto.adapter.dto;

import lombok.Builder;

@Builder
public record ServiceHealthCheckResponse(
        Boolean failing,
        Integer minResponseTime
) {
}
