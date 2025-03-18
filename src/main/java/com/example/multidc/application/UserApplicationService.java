package com.example.multidc.application;

import com.example.multidc.domain.user.User;
import com.example.multidc.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    
    private final UserRepository userRepository;

    @Transactional
    public User createUser(String username, String email) {
        String datacenter = System.getProperty("datacenter.id");
        User user = new User(username, email, datacenter);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, String username, String email) {
        String datacenter = System.getProperty("datacenter.id");
        User user = userRepository.findByIdAndDatacenter(id, datacenter)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user, datacenter);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        userRepository.delete(user);
    }

    @Transactional
    public User replicateUser(Long userId, String username, String email, String sourceDatacenter) {
        String targetDatacenter = System.getProperty("datacenter.id");
        User user = new User(username, email, targetDatacenter);
        user.setId(userId);
        return userRepository.save(user, targetDatacenter);
    }

    @Transactional
    public void deleteUser(Long id, String datacenter) {
        userRepository.findByIdAndDatacenter(id, datacenter)
            .ifPresent(user -> userRepository.delete(user, datacenter));
    }
} 