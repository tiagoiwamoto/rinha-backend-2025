package io.github.tiagoiwamoto.core;

import io.github.tiagoiwamoto.adapter.PaymentProcessorDefaultAdapter;
import io.github.tiagoiwamoto.adapter.PaymentProcessorFallbackAdapter;
import io.github.tiagoiwamoto.adapter.dto.PaymentProcessResponse;
import io.github.tiagoiwamoto.adapter.dto.ServiceHealthCheckResponse;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PaymentUsecase {

    @Inject
    @RestClient
    PaymentProcessorDefaultAdapter processorDefaultAdapter;
    @Inject
    @RestClient
    PaymentProcessorFallbackAdapter processorFallbackAdapter;


    public String checkHealth() {
        var responseDefault = processorDefaultAdapter.checkServiceHealth().readEntity(ServiceHealthCheckResponse.class);
        var responseFallback = processorFallbackAdapter.checkServiceHealth().readEntity(ServiceHealthCheckResponse.class);
        if(responseDefault.failing() || responseDefault.minResponseTime() > responseFallback.minResponseTime()) {
            return "FALLBACK";
        }
        return "DEFAULT";
    }

    public void sendData(PaymentRequest payload) {
        if(checkHealth().equals("FALLBACK")) {
            // Fallback to the fallback adapter
            var response = processorFallbackAdapter.processPayment(payload).readEntity(PaymentProcessResponse.class);
        }else {
            var response = processorDefaultAdapter.processPayment(payload).readEntity(PaymentProcessResponse.class);
        }
    }
}
