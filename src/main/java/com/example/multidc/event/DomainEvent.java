package com.example.multidc.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainEvent {
    private UUID eventId;
    private String eventType;
    private String entityType;
    private String entityId;
    private String datacenter;
    private Instant timestamp;
    private String payload;
    private Long version;

    protected DomainEvent(String eventType, String entityType, String entityId, String datacenter, String payload) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.datacenter = datacenter;
        this.timestamp = Instant.now();
        this.payload = payload;
        this.version = 1L;
    }
} 