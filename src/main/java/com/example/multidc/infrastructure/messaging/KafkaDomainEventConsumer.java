package com.example.multidc.infrastructure.messaging;

import com.example.multidc.application.UserApplicationService;
import com.example.multidc.domain.user.events.UserCreatedEvent;
import com.example.multidc.domain.user.events.UserUpdatedEvent;
import com.example.multidc.domain.user.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaDomainEventConsumer {
    
    private final UserApplicationService userService;

    @KafkaListener(topics = "multidc.events.usercreated", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreated(UserCreatedEvent event) {
        // Skip events from the same datacenter to avoid infinite loops
        if (event.getDatacenter().equals(System.getProperty("datacenter.id"))) {
            return;
        }

        userService.replicateUser(
            event.getUserId(),
            event.getUsername(),
            event.getEmail(),
            event.getDatacenter()
        );
    }

    @KafkaListener(topics = "multidc.events.userupdated", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserUpdated(UserUpdatedEvent event) {
        if (event.getDatacenter().equals(System.getProperty("datacenter.id"))) {
            return;
        }

        userService.replicateUser(
            event.getUserId(),
            event.getUsername(),
            event.getEmail(),
            event.getDatacenter()
        );
    }

    @KafkaListener(topics = "multidc.events.userdeleted", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserDeleted(UserDeletedEvent event) {
        if (event.getDatacenter().equals(System.getProperty("datacenter.id"))) {
            return;
        }

        userService.deleteUser(event.getUserId(), event.getDatacenter());
    }
} 