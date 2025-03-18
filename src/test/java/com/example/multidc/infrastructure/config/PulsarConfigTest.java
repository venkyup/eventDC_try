package com.example.multidc.infrastructure.config;

import org.apache.pulsar.client.api.PulsarClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PulsarConfigTest {

    @InjectMocks
    private PulsarConfig pulsarConfig;

    @Test
    void pulsarClientPropertiesShouldCreateDefaultProperties() {
        // Act
        PulsarClientProperties properties = pulsarConfig.pulsarClientProperties();

        // Assert
        assertEquals("pulsar://localhost:6650", properties.getServiceUrl());
        assertEquals(30000, properties.getOperationTimeoutMs());
        assertEquals(30000, properties.getConnectionTimeoutMs());
        assertTrue(properties.isEnableTcpNoDelay());
        assertEquals(20, properties.getKeepAliveIntervalSeconds());
    }

    @Test
    void pulsarClientShouldCreateClientWithCustomProperties() throws Exception {
        // Arrange
        PulsarClientProperties properties = new PulsarClientProperties();
        properties.setServiceUrl("pulsar://custom:6650");
        properties.setOperationTimeoutMs(5000);
        properties.setConnectionTimeoutMs(3000);
        properties.setEnableTcpNoDelay(false);
        properties.setKeepAliveIntervalSeconds(30);

        // Act
        PulsarClient client = pulsarConfig.pulsarClient(properties);

        // Assert
        assertNotNull(client);
    }

    @Test
    void pulsarClientPropertiesShouldAllowCustomization() {
        // Arrange
        PulsarClientProperties properties = pulsarConfig.pulsarClientProperties();
        
        // Act
        properties.setServiceUrl("pulsar://custom:6650");
        properties.setOperationTimeoutMs(5000);
        properties.setConnectionTimeoutMs(3000);
        properties.setEnableTcpNoDelay(false);
        properties.setKeepAliveIntervalSeconds(30);

        // Assert
        assertEquals("pulsar://custom:6650", properties.getServiceUrl());
        assertEquals(5000, properties.getOperationTimeoutMs());
        assertEquals(3000, properties.getConnectionTimeoutMs());
        assertFalse(properties.isEnableTcpNoDelay());
        assertEquals(30, properties.getKeepAliveIntervalSeconds());
    }
} 