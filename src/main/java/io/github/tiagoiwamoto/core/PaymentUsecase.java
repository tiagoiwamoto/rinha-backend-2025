package io.github.tiagoiwamoto.core;

import io.github.tiagoiwamoto.adapter.PaymentProcessorDefaultAdapter;
import io.github.tiagoiwamoto.adapter.PaymentProcessorFallbackAdapter;
import io.github.tiagoiwamoto.config.GlobalConfig;
import io.github.tiagoiwamoto.core.port.PaymentProcessorStrategy;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class PaymentUsecase {

    @Inject
    PaymentProcessorDefaultAdapter processorDefaultAdapter;
    @Inject
    PaymentProcessorFallbackAdapter processorFallbackAdapter;
    private Map<String, PaymentProcessorStrategy> strategies;

    public String checkHealth() {
        var responseDefault = processorDefaultAdapter.checkServiceHealth();
        var responseFallback = processorFallbackAdapter.checkServiceHealth();
        if(responseDefault.getBody().failing() ||
                responseDefault.getBody().minResponseTime() >
                        responseFallback.getBody().minResponseTime()) {
            return "FALLBACK";
        }
        return "DEFAULT";
    }

    public void sendData(PaymentRequest payload) {
        this.strategies = new HashMap<>();
        this.strategies.put("DEFAULT", processorDefaultAdapter);
        this.strategies.put("FALLBACK", processorFallbackAdapter);
        String server = GlobalConfig.getServiceStatus();
        log.info("melhor servidor para realizar o pagamento: {}", server);
        var service = strategies.get(server);
        var response = service.processPayment(payload);
        log.info("resposta do servidor {}: {}", server, response.getStatus());
    }
}
