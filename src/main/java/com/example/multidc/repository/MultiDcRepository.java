package com.example.multidc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

@NoRepositoryBean
public interface MultiDcRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    
    /**
     * Saves an entity to the specified datacenter.
     *
     * @param entity the entity to save
     * @param datacenter the datacenter identifier
     * @return the saved entity
     */
    T saveToDatacenter(T entity, String datacenter);
    
    /**
     * Finds an entity by its ID in the specified datacenter.
     *
     * @param id the entity ID
     * @param datacenter the datacenter identifier
     * @return the entity if found, null otherwise
     */
    T findByIdInDatacenter(ID id, String datacenter);
    
    /**
     * Deletes an entity from the specified datacenter.
     *
     * @param entity the entity to delete
     * @param datacenter the datacenter identifier
     */
    void deleteFromDatacenter(T entity, String datacenter);
    
    /**
     * Checks if an entity exists in the specified datacenter.
     *
     * @param id the entity ID
     * @param datacenter the datacenter identifier
     * @return true if the entity exists, false otherwise
     */
    boolean existsInDatacenter(ID id, String datacenter);
} 