package com.example.multidc.infrastructure.messaging;

import com.example.multidc.domain.shared.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaDomainEventPublisher implements DomainEventPublisher {
    
    private static final String TOPIC_PREFIX = "multidc.events.";
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Override
    public void publish(DomainEvent event) {
        String topic = TOPIC_PREFIX + event.getClass().getSimpleName().toLowerCase()
            .replace("event", "");
        
        kafkaTemplate.send(topic, event.getEventId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    throw new RuntimeException("Failed to publish event: " + event.getEventId(), ex);
                }
            });
    }
} 