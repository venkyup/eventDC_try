package com.example.multidc.infrastructure.cdc;

import com.example.multidc.domain.user.User;
import com.example.multidc.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatabaseChangeEventHandler {
    
    private final UserRepository userRepository;

    @KafkaListener(topics = "${debezium.topic.prefix}.users", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handleDatabaseChange(DatabaseChangeEvent event) {
        // Skip events from the same datacenter
        if (event.getSource().equals(System.getProperty("datacenter.id"))) {
            return;
        }

        switch (event.getOp()) {
            case "c", "u" -> handleCreateOrUpdate(event.getAfter());
            case "d" -> handleDelete(event.getBefore());
        }
    }

    private void handleCreateOrUpdate(DatabaseChangeEvent.Payload payload) {
        User user = new User();
        user.setId(payload.getId());
        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setDatacenter(System.getProperty("datacenter.id")); // Set local datacenter
        user.setVersion(payload.getVersion());
        userRepository.save(user);
    }

    private void handleDelete(DatabaseChangeEvent.Payload payload) {
        userRepository.findByIdAndDatacenter(payload.getId(), System.getProperty("datacenter.id"))
            .ifPresent(userRepository::delete);
    }
} 