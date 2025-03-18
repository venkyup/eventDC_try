package com.example.multidc.domain.shared;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for all domain events in the system.
 * Provides common attributes and functionality for event tracking.
 */
@Getter
public abstract class DomainEvent {
    /** Unique identifier for the event */
    private final UUID eventId;
    /** Type of the event */
    private final String eventType;
    /** Timestamp when the event occurred */
    private final Instant timestamp;
    /** Identifier of the datacenter where the event originated */
    private final String datacenter;

    /**
     * Creates a new domain event.
     *
     * @param eventType The type of the event
     * @param datacenter The identifier of the datacenter where the event occurred
     */
    protected DomainEvent(String eventType, String datacenter) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.datacenter = datacenter;
    }
} 