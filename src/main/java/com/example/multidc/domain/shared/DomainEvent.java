package com.example.multidc.domain.shared;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class DomainEvent {
    private final UUID eventId;
    private final String eventType;
    private final Instant timestamp;
    private final String datacenter;

    protected DomainEvent(String eventType, String datacenter) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.datacenter = datacenter;
    }
} 