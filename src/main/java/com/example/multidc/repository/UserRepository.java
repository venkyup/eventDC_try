package com.example.multidc.repository;

import com.example.multidc.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MultiDcRepository<User, Long> {
    // Additional custom query methods can be added here
} 