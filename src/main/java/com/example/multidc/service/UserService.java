package com.example.multidc.service;

import com.example.multidc.entity.User;
import com.example.multidc.repository.UserRepository;
import com.example.multidc.event.EventPublisher;
import com.example.multidc.event.UserEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public User createUser(User user, String datacenter) {
        user.setDatacenter(datacenter);
        User savedUser = userRepository.saveToDatacenter(user, datacenter);
        eventPublisher.publish(UserEvent.created(savedUser, datacenter));
        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> findUser(Long id, String datacenter) {
        User user = userRepository.findByIdInDatacenter(id, datacenter);
        return Optional.ofNullable(user);
    }

    @Transactional
    public void deleteUser(Long id, String datacenter) {
        User user = userRepository.findByIdInDatacenter(id, datacenter);
        if (user != null) {
            userRepository.deleteFromDatacenter(user, datacenter);
            eventPublisher.publish(UserEvent.deleted(user, datacenter));
        }
    }

    @Transactional(readOnly = true)
    public boolean userExists(Long id, String datacenter) {
        return userRepository.existsInDatacenter(id, datacenter);
    }

    @Transactional
    public User updateUser(User user, String datacenter) {
        if (userRepository.existsInDatacenter(user.getId(), datacenter)) {
            User updatedUser = userRepository.saveToDatacenter(user, datacenter);
            eventPublisher.publish(UserEvent.updated(updatedUser, datacenter));
            return updatedUser;
        }
        throw new IllegalArgumentException("User not found in datacenter: " + datacenter);
    }

    @Transactional
    public User replicateUser(User user, String sourceDatacenter) {
        String targetDatacenter = System.getProperty("datacenter.id");
        user.setDatacenter(targetDatacenter);
        return userRepository.saveToDatacenter(user, targetDatacenter);
    }
} 