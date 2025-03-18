package com.example.multidc.domain.user.events;

import com.example.multidc.domain.shared.DomainEvent;
import com.example.multidc.domain.user.User;
import lombok.Getter;

@Getter
public class UserUpdatedEvent extends DomainEvent {
    private final Long userId;
    private final String username;
    private final String email;
    private final Long version;

    public UserUpdatedEvent(User user) {
        super("USER_UPDATED", user.getDatacenter());
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.version = user.getVersion();
    }
} 