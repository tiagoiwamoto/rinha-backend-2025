package io.github.tiagoiwamoto.config;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GlobalConfig {

    private static String serviceStatus;

    public static String getServiceStatus() {
        return serviceStatus;
    }

    public static void setServiceStatus(String status) {
        serviceStatus = status;
    }
}