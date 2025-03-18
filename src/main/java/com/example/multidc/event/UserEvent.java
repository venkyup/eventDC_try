package com.example.multidc.event;

import com.example.multidc.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

/**
 * Event class representing user-related domain events.
 * Handles serialization and deserialization of user events.
 */
@Getter
public class UserEvent extends DomainEvent {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Enumeration of possible user event types.
     */
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

    /**
     * Creates a new user event.
     *
     * @param type The type of the event
     * @param user The user associated with the event
     * @param datacenter The datacenter where the event occurred
     */
    private UserEvent(EventType type, User user, String datacenter) {
        super(
            type.name(),
            User.class.getSimpleName(),
            user.getId().toString(),
            datacenter,
            serializePayload(user)
        );
    }

    /**
     * Serializes a user object to JSON string.
     *
     * @param user The user to serialize
     * @return JSON string representation of the user
     * @throws RuntimeException if serialization fails
     */
    private static String serializePayload(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize user event", e);
        }
    }

    /**
     * Creates a user creation event.
     *
     * @param user The created user
     * @param datacenter The datacenter where the user was created
     * @return A new UserEvent instance for user creation
     */
    public static UserEvent created(User user, String datacenter) {
        return new UserEvent(EventType.CREATED, user, datacenter);
    }

    /**
     * Creates a user update event.
     *
     * @param user The updated user
     * @param datacenter The datacenter where the user was updated
     * @return A new UserEvent instance for user update
     */
    public static UserEvent updated(User user, String datacenter) {
        return new UserEvent(EventType.UPDATED, user, datacenter);
    }

    /**
     * Creates a user deletion event.
     *
     * @param user The deleted user
     * @param datacenter The datacenter where the user was deleted
     * @return A new UserEvent instance for user deletion
     */
    public static UserEvent deleted(User user, String datacenter) {
        return new UserEvent(EventType.DELETED, user, datacenter);
    }

    /**
     * Gets the user object from the event payload.
     *
     * @return The deserialized user object
     * @throws RuntimeException if deserialization fails
     */
    public User getUser() {
        try {
            return objectMapper.readValue(getPayload(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize user from event", e);
        }
    }
} 