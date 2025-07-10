package io.github.tiagoiwamoto.adapter;

import io.github.tiagoiwamoto.config.GlobalConfig;
import io.github.tiagoiwamoto.core.PaymentUsecase;
import io.github.tiagoiwamoto.entrypoint.dto.PaymentRequest;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class HealthCheckAdapter {

    @Inject
    PaymentUsecase usecase;

    @Scheduled(every = "1s") // Executa a cada 10 segundos
    public void checkHealth() {
        try {
            // Caminho para o script.sh
            String scriptPath = "./script.sh";

            // Configura o ProcessBuilder para executar o script
            ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath);
            processBuilder.redirectErrorStream(true);

            // Inicia o processo
            Process process = processBuilder.start();
            // Lê a saída do script
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("Linha recuperada: {}", line);
                    System.setProperty("SERVICE_STATUS", line);
                    GlobalConfig.setServiceStatus(line); // Salva o valor em uma classe singleton
                }
            }

            // Aguarda o término do processo
            int exitCode = process.waitFor();
            String server = System.getProperty("SERVICE_STATUS");
            log.info("Script finalizado com código de saída: {}, melhor servidor para realizar o pagamento: {}", exitCode, server);
            usecase.sendData(PaymentRequest.builder()
                    .requestedAt(LocalDateTime.now())
                    .amount(BigDecimal.valueOf(1500.0))
                    .correlationId(UUID.randomUUID())
                    .build());
        } catch (Exception e) {
            log.error("Falha ao executar script, uma nova tentativa será efetuada no próximo agendamento. ERRO: {}", e.getMessage());
        }
    }

}
