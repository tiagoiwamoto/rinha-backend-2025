package io.github.tiagoiwamoto.adapter;

import io.github.tiagoiwamoto.adapter.dto.PaymentProcessResponse;
import io.github.tiagoiwamoto.adapter.dto.ServiceHealthCheckResponse;
import io.github.tiagoiwamoto.core.port.PaymentProcessorStrategy;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

@ApplicationScoped
public class PaymentProcessorFallbackAdapter implements PaymentProcessorStrategy {

    private final String BASE_URL = "http://localhost:8002";

    public HttpResponse<ServiceHealthCheckResponse> checkServiceHealth(){
        HttpResponse<ServiceHealthCheckResponse> response = Unirest.get(BASE_URL.concat("/service-health"))
                .asObject(ServiceHealthCheckResponse.class);

        return response;
    }

    public HttpResponse<PaymentProcessResponse> processPayment(PaymentRequest payload){
        HttpResponse<PaymentProcessResponse> response = Unirest.post(BASE_URL.concat("/payments"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(payload)
                .asObject(PaymentProcessResponse.class);
        return response;
    }

    public Boolean supports(String server, Integer maxAttempts) {
        return "FALLBACK".equals(server) && maxAttempts.equals(3);
    }
}
