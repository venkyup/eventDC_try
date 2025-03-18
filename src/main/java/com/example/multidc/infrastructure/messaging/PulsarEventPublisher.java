package com.example.multidc.infrastructure.messaging;

import com.example.multidc.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulsarEventPublisher {
    private static final String TOPIC_PREFIX = "persistent://public/default/multidc.events.";
    
    private final PulsarClient pulsarClient;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, Producer<String>> producers = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws Exception {
        // Pre-create producers for known event types
        createProducer("usercreated");
        createProducer("userupdated");
        createProducer("userdeleted");
    }

    private void createProducer(String eventType) throws Exception {
        Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
            .topic(TOPIC_PREFIX + eventType)
            .enableBatching(true)
            .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
            .blockIfQueueFull(true)
            .create();
        producers.put(eventType, producer);
    }

    public void publish(DomainEvent event) {
        try {
            String eventType = event.getEntityType().toLowerCase();
            Producer<String> producer = producers.computeIfAbsent(eventType, k -> {
                try {
                    return pulsarClient.newProducer(Schema.STRING)
                        .topic(TOPIC_PREFIX + k)
                        .create();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create producer for " + k, e);
                }
            });

            String message = objectMapper.writeValueAsString(event);
            producer.newMessage()
                .key(event.getEntityId())
                .value(message)
                .sendAsync()
                .whenComplete((msgId, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event: " + event.getEventId(), ex);
                    } else {
                        log.debug("Published event: {} with messageId: {}", event.getEventId(), msgId);
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event: " + event.getEventId(), e);
        }
    }

    @PreDestroy
    public void cleanup() {
        producers.values().forEach(producer -> {
            try {
                producer.close();
            } catch (Exception e) {
                log.error("Error closing producer", e);
            }
        });
    }
} 