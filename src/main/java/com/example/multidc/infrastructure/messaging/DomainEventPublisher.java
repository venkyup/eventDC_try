package com.example.multidc.infrastructure.messaging;

import com.example.multidc.domain.shared.DomainEvent;

/**
 * Interface for publishing domain events.
 * Implementations of this interface handle the distribution of domain events across the system.
 */
public interface DomainEventPublisher {
    /**
     * Publishes a domain event to the messaging system.
     *
     * @param event The domain event to publish
     */
    void publish(DomainEvent event);
} 