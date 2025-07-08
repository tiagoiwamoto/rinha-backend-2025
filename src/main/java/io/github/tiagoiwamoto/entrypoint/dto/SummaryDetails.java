package io.github.tiagoiwamoto.entrypoint.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SummaryDetails(
        Integer totalRequests,
        BigDecimal totalAmount
) {
}
