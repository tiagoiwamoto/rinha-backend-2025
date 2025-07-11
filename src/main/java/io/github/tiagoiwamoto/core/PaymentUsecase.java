package io.github.tiagoiwamoto.core;

import io.github.tiagoiwamoto.adapter.PaymentProcessorDefaultAdapter;
import io.github.tiagoiwamoto.adapter.PaymentProcessorFallbackAdapter;
import io.github.tiagoiwamoto.config.GlobalConfig;
import io.github.tiagoiwamoto.core.error.RetryAttempException;
import io.github.tiagoiwamoto.core.port.PaymentProcessorStrategy;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class PaymentUsecase {

    @Inject
    PaymentProcessorDefaultAdapter processorDefaultAdapter;
    @Inject
    PaymentProcessorFallbackAdapter processorFallbackAdapter;
    private List<PaymentProcessorStrategy> strategies;

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
        try{
            this.strategies = List.of();
            this.strategies.add(processorDefaultAdapter);
            this.strategies.add(processorFallbackAdapter);
            String server = GlobalConfig.getServiceStatus();
            log.info("melhor servidor para realizar o pagamento: {}", server);
            var service = strategies.stream()
                    .filter(st -> st.supports(server, GlobalConfig.getMaxAttempts()))
                    .findFirst()
                    .orElseThrow(RetryAttempException::new);
            var response = service.processPayment(payload);
            log.info("resposta do servidor {}: {}", server, response.getStatus());
        }catch (RetryAttempException e){
            GlobalConfig.setMaxAttempts(GlobalConfig.getMaxAttempts() + 1);
            sendData(payload);
        }catch (Exception e){
            log.error("Erro ao processar pagamento: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar pagamento", e);
        }
    }
}
