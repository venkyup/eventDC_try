package com.example.multidc.domain.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for aggregate roots in the domain model.
 * Provides functionality for tracking and managing domain events.
 */
public abstract class AggregateRoot {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Registers a new domain event for this aggregate.
     *
     * @param event The domain event to register
     */
    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * Returns an unmodifiable list of all registered domain events.
     *
     * @return List of domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clears all registered domain events.
     * Typically called after events have been processed.
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }
} 