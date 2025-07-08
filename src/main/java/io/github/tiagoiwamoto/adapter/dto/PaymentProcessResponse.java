package io.github.tiagoiwamoto.adapter.dto;

import lombok.Builder;

@Builder
public record PaymentProcessResponse(
        String message
) {
}
