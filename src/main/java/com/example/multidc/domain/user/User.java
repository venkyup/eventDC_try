package com.example.multidc.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a user in the system.
 * This entity is replicated across multiple datacenters.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Username of the user */
    private String username;
    
    /** Email address of the user */
    private String email;
    
    /** Identifier of the datacenter where this user record originated */
    private String datacenter;
    
    /** Version number for optimistic locking */
    @Version
    private Long version;

    /**
     * Creates a new user with the specified details.
     *
     * @param username The username of the user
     * @param email The email address of the user
     * @param datacenter The identifier of the datacenter where this user is created
     */
    public User(String username, String email, String datacenter) {
        this.username = username;
        this.email = email;
        this.datacenter = datacenter;
    }
} 