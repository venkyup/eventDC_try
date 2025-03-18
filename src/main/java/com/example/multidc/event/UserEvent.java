package com.example.multidc.event;

import com.example.multidc.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class UserEvent extends DomainEvent {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

    private UserEvent(EventType type, User user, String datacenter) {
        super(
            type.name(),
            User.class.getSimpleName(),
            user.getId().toString(),
            datacenter,
            serializePayload(user)
        );
    }

    private static String serializePayload(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize user event", e);
        }
    }

    public static UserEvent created(User user, String datacenter) {
        return new UserEvent(EventType.CREATED, user, datacenter);
    }

    public static UserEvent updated(User user, String datacenter) {
        return new UserEvent(EventType.UPDATED, user, datacenter);
    }

    public static UserEvent deleted(User user, String datacenter) {
        return new UserEvent(EventType.DELETED, user, datacenter);
    }

    public User getUser() {
        try {
            return objectMapper.readValue(getPayload(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize user from event", e);
        }
    }
} 