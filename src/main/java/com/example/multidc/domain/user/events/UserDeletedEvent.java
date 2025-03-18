package com.example.multidc.domain.user.events;

import com.example.multidc.domain.shared.DomainEvent;
import com.example.multidc.domain.user.User;
import lombok.Getter;

@Getter
public class UserDeletedEvent extends DomainEvent {
    private final Long userId;

    public UserDeletedEvent(User user) {
        super("USER_DELETED", user.getDatacenter());
        this.userId = user.getId();
    }
} 