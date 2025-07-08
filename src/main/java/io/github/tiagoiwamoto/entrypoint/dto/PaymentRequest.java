package io.github.tiagoiwamoto.entrypoint.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentRequest(
        UUID correlationId,
        BigDecimal amount,
        LocalDateTime requestedAt
) {
}
