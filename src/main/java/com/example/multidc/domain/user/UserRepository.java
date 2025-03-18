package com.example.multidc.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDatacenter(Long id, String datacenter);
    User save(User user, String datacenter);
    void delete(User user, String datacenter);
    boolean exists(Long id, String datacenter);
} 