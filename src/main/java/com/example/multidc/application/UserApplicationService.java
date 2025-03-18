package com.example.multidc.application;

import com.example.multidc.domain.user.User;
import com.example.multidc.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for managing user operations.
 * Handles user creation, updates, deletion, and replication across datacenters.
 */
@Service
@RequiredArgsConstructor
public class UserApplicationService {
    
    private final UserRepository userRepository;

    /**
     * Creates a new user in the current datacenter.
     *
     * @param username The username for the new user
     * @param email The email address for the new user
     * @return The created user
     */
    @Transactional
    public User createUser(String username, String email) {
        String datacenter = System.getProperty("datacenter.id");
        User user = new User(username, email, datacenter);
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the current datacenter.
     *
     * @param id The ID of the user to update
     * @param username The new username
     * @param email The new email address
     * @return The updated user
     * @throws IllegalArgumentException if the user is not found
     */
    @Transactional
    public User updateUser(Long id, String username, String email) {
        String datacenter = System.getProperty("datacenter.id");
        User user = userRepository.findByIdAndDatacenter(id, datacenter)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user, datacenter);
    }

    /**
     * Deletes a user from the current datacenter.
     *
     * @param id The ID of the user to delete
     * @throws IllegalArgumentException if the user is not found
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        userRepository.delete(user);
    }

    /**
     * Replicates a user from one datacenter to another.
     *
     * @param userId The ID of the user to replicate
     * @param username The username to replicate
     * @param email The email to replicate
     * @param sourceDatacenter The source datacenter identifier
     * @return The replicated user
     */
    @Transactional
    public User replicateUser(Long userId, String username, String email, String sourceDatacenter) {
        String targetDatacenter = System.getProperty("datacenter.id");
        User user = new User(username, email, targetDatacenter);
        user.setId(userId);
        return userRepository.save(user, targetDatacenter);
    }

    /**
     * Deletes a user from a specific datacenter.
     *
     * @param id The ID of the user to delete
     * @param datacenter The datacenter identifier
     */
    @Transactional
    public void deleteUser(Long id, String datacenter) {
        userRepository.findByIdAndDatacenter(id, datacenter)
            .ifPresent(user -> userRepository.delete(user, datacenter));
    }
} 