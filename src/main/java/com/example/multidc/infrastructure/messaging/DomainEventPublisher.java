package com.example.multidc.infrastructure.messaging;

import com.example.multidc.domain.shared.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
} 