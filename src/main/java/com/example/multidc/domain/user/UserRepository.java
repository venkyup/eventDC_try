package com.example.multidc.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides methods for managing user data across multiple datacenters.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their ID and datacenter.
     *
     * @param id The user ID
     * @param datacenter The datacenter identifier
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByIdAndDatacenter(Long id, String datacenter);

    /**
     * Saves a user in the specified datacenter.
     *
     * @param user The user to save
     * @param datacenter The datacenter identifier
     * @return The saved user
     */
    User save(User user, String datacenter);

    /**
     * Deletes a user from the specified datacenter.
     *
     * @param user The user to delete
     * @param datacenter The datacenter identifier
     */
    void delete(User user, String datacenter);

    /**
     * Checks if a user exists in the specified datacenter.
     *
     * @param id The user ID
     * @param datacenter The datacenter identifier
     * @return true if the user exists, false otherwise
     */
    boolean exists(Long id, String datacenter);
} 