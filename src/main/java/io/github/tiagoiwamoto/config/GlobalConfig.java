package io.github.tiagoiwamoto.config;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

@ApplicationScoped
public class GlobalConfig {

    private static String serviceStatus;
    private static Integer maxAttempts;

    public static String getServiceStatus() {
        return serviceStatus;
    }

    public static void setServiceStatus(String status) {
        serviceStatus = status;
    }

    public static Integer getMaxAttempts() {
        return Objects.isNull(maxAttempts) ? 0 : maxAttempts;
    }

    public static void setMaxAttempts(Integer maxAttempts) {
        try {
            Thread.sleep(550L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GlobalConfig.maxAttempts = maxAttempts;
    }
}