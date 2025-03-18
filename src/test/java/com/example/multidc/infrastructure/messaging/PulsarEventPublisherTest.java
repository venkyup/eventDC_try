package com.example.multidc.infrastructure.messaging;

import com.example.multidc.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PulsarEventPublisherTest {

    @Mock
    private PulsarClient pulsarClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Producer<String> producer;

    @Mock
    private TypedMessageBuilder<String> messageBuilder;

    private PulsarEventPublisher publisher;

    @BeforeEach
    void setUp() throws Exception {
        // Setup producer mock chain
        @SuppressWarnings("unchecked")
        ProducerBuilder<String> producerBuilder = mock(ProducerBuilder.class);
        when(pulsarClient.newProducer(Schema.STRING))
            .thenReturn(producerBuilder);
        when(producerBuilder.topic(anyString()))
            .thenReturn(producerBuilder);
        when(producerBuilder.enableBatching(anyBoolean()))
            .thenReturn(producerBuilder);
        when(producerBuilder.batchingMaxPublishDelay(anyLong(), any(TimeUnit.class)))
            .thenReturn(producerBuilder);
        when(producerBuilder.blockIfQueueFull(anyBoolean()))
            .thenReturn(producerBuilder);
        when(producerBuilder.create())
            .thenReturn(producer);

        // Setup message builder mock chain
        when(producer.newMessage()).thenReturn(messageBuilder);
        when(messageBuilder.key(anyString())).thenReturn(messageBuilder);
        when(messageBuilder.value(anyString())).thenReturn(messageBuilder);
        when(messageBuilder.sendAsync()).thenReturn(CompletableFuture.completedFuture(mock(MessageId.class)));

        publisher = new PulsarEventPublisher(pulsarClient, objectMapper);
    }

    @Test
    void initShouldCreateProducersForKnownEventTypes() throws Exception {
        // Act
        publisher.init();

        // Assert
        verify(pulsarClient, times(3)).newProducer(Schema.STRING);
        verify(producer, never()).close();
    }

    @Test
    void publishShouldSendEventSuccessfully() throws Exception {
        // Arrange
        DomainEvent event = mock(DomainEvent.class);
        UUID eventId = UUID.randomUUID();
        String entityId = "entity-" + System.currentTimeMillis();
        when(event.getEntityType()).thenReturn("TestEntity");
        when(event.getEntityId()).thenReturn(entityId);
        when(event.getEventId()).thenReturn(eventId);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Act
        publisher.publish(event);

        // Assert
        verify(messageBuilder).key(entityId);
        verify(messageBuilder).value("{}");
        verify(messageBuilder).sendAsync();
    }

    @Test
    void publishShouldHandleJsonSerializationError() throws Exception {
        // Arrange
        DomainEvent event = mock(DomainEvent.class);
        when(event.getEntityType()).thenReturn("TestEntity");
        when(event.getEventId()).thenReturn(UUID.randomUUID());
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> publisher.publish(event));
    }

    @Test
    void publishShouldHandleProducerCreationError() throws Exception {
        // Arrange
        DomainEvent event = mock(DomainEvent.class);
        when(event.getEntityType()).thenReturn("TestEntity");
        when(pulsarClient.newProducer(Schema.STRING).topic(anyString()).create())
            .thenThrow(new RuntimeException("Producer creation failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> publisher.publish(event));
    }

    @Test
    void cleanupShouldCloseAllProducers() throws Exception {
        // Arrange
        publisher.init();

        // Act
        publisher.cleanup();

        // Assert
        verify(producer, times(3)).close();
    }

    @Test
    void cleanupShouldHandleProducerCloseError() throws Exception {
        // Arrange
        publisher.init();
        doThrow(new RuntimeException("Close failed")).when(producer).close();

        // Act
        publisher.cleanup();

        // Assert
        verify(producer, times(3)).close();
    }
} 