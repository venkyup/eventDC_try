package com.example.multidc.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class PulsarConfig {

    @Bean
    @ConfigurationProperties(prefix = "pulsar.client")
    public PulsarClientProperties pulsarClientProperties() {
        return new PulsarClientProperties();
    }

    @Bean
    public PulsarClient pulsarClient(PulsarClientProperties properties) throws Exception {
        return PulsarClient.builder()
            .serviceUrl(properties.getServiceUrl())
            .operationTimeout(properties.getOperationTimeoutMs(), TimeUnit.MILLISECONDS)
            .connectionTimeout(properties.getConnectionTimeoutMs(), TimeUnit.MILLISECONDS)
            .enableTcpNoDelay(properties.isEnableTcpNoDelay())
            .keepAliveInterval(properties.getKeepAliveIntervalSeconds(), TimeUnit.SECONDS)
            .build();
    }
}

@ConfigurationProperties(prefix = "pulsar")
class PulsarClientProperties {
    private String serviceUrl = "pulsar://localhost:6650";
    private int operationTimeoutMs = 30000;
    private int connectionTimeoutMs = 30000;
    private boolean enableTcpNoDelay = true;
    private int keepAliveIntervalSeconds = 20;

    // Getters and setters
    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
    public int getOperationTimeoutMs() { return operationTimeoutMs; }
    public void setOperationTimeoutMs(int operationTimeoutMs) { this.operationTimeoutMs = operationTimeoutMs; }
    public int getConnectionTimeoutMs() { return connectionTimeoutMs; }
    public void setConnectionTimeoutMs(int connectionTimeoutMs) { this.connectionTimeoutMs = connectionTimeoutMs; }
    public boolean isEnableTcpNoDelay() { return enableTcpNoDelay; }
    public void setEnableTcpNoDelay(boolean enableTcpNoDelay) { this.enableTcpNoDelay = enableTcpNoDelay; }
    public int getKeepAliveIntervalSeconds() { return keepAliveIntervalSeconds; }
    public void setKeepAliveIntervalSeconds(int keepAliveIntervalSeconds) { this.keepAliveIntervalSeconds = keepAliveIntervalSeconds; }
} 