package com.example.multidc.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {
    private static final String TOPIC_PREFIX = "multidc.events.";
    
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public void publish(DomainEvent event) {
        String topic = TOPIC_PREFIX + event.getEntityType().toLowerCase();
        String key = event.getEntityId();
        
        kafkaTemplate.send(topic, key, event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    throw new RuntimeException("Failed to publish event: " + event.getEventId(), ex);
                }
            });
    }
} 