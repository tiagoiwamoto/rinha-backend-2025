package io.github.tiagoiwamoto.adapter;

import io.github.tiagoiwamoto.adapter.dto.PaymentProcessResponse;
import io.github.tiagoiwamoto.adapter.dto.ServiceHealthCheckResponse;
import io.github.tiagoiwamoto.core.port.PaymentProcessorStrategy;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

@ApplicationScoped
public class PaymentProcessorDefaultAdapter implements PaymentProcessorStrategy {

    private final String BASE_URL = "http://localhost:8001";

    public HttpResponse<ServiceHealthCheckResponse> checkServiceHealth(){
        HttpResponse<ServiceHealthCheckResponse> response = Unirest.get(BASE_URL.concat("/service-health"))
                .queryString("fruit", "apple")
                .queryString("droid", "R2D2")
                .asObject(ServiceHealthCheckResponse.class);

        return response;
    }

    public HttpResponse<PaymentProcessResponse> processPayment(PaymentRequest payload){
        HttpResponse<PaymentProcessResponse> response = Unirest.post(BASE_URL.concat("/payments"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("token", "123")
                .body(payload)
                .asObject(PaymentProcessResponse.class);
        return response;
    }

    public Boolean supports(String server, Integer maxAttempts) {
        return "DEFAULT".equals(server);
    }

}
