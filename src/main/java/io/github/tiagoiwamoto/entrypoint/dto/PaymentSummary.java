package io.github.tiagoiwamoto.entrypoint.dto;

import lombok.Builder;

@Builder
public record PaymentSummary(
        SummaryDetails defaultSummary,
        SummaryDetails fallbackSummary
) {
}
