package io.github.tiagoiwamoto.core.port;

import io.github.tiagoiwamoto.adapter.dto.PaymentProcessResponse;
import io.github.tiagoiwamoto.adapter.dto.ServiceHealthCheckResponse;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.ws.rs.core.Response;
import kong.unirest.core.HttpResponse;

public interface PaymentProcessorStrategy {

    HttpResponse<ServiceHealthCheckResponse> checkServiceHealth();

    HttpResponse<PaymentProcessResponse> processPayment(PaymentRequest payload);

    Boolean supports(String server, Integer maxAttempts);

}
