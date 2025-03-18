package com.example.multidc.event;

import com.example.multidc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventConsumer {
    
    private final UserService userService;

    @KafkaListener(topics = "multidc.events.user", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserEvent(UserEvent event) {
        // Skip events from the same datacenter to avoid infinite loops
        if (event.getDatacenter().equals(System.getProperty("datacenter.id"))) {
            return;
        }

        switch (UserEvent.EventType.valueOf(event.getEventType())) {
            case CREATED:
            case UPDATED:
                userService.replicateUser(event.getUser(), event.getDatacenter());
                break;
            case DELETED:
                userService.deleteUser(Long.valueOf(event.getEntityId()), event.getDatacenter());
                break;
        }
    }
} 