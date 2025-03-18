package com.example.multidc.domain.user.events;

import com.example.multidc.domain.shared.DomainEvent;
import com.example.multidc.domain.user.User;
import lombok.Getter;

@Getter
public class UserCreatedEvent extends DomainEvent {
    private final Long userId;
    private final String username;
    private final String email;

    public UserCreatedEvent(User user) {
        super("USER_CREATED", user.getDatacenter());
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
} 