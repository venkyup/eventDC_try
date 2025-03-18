package com.example.multidc.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiDcRepositoryImpl<T, ID extends Serializable> 
        extends SimpleJpaRepository<T, ID> 
        implements MultiDcRepository<T, ID> {

    private final Map<String, EntityManager> dataCenterEntityManagers;
    private final Class<T> domainClass;

    public MultiDcRepositoryImpl(Class<T> domainClass, EntityManager primaryEntityManager) {
        super(domainClass, primaryEntityManager);
        this.dataCenterEntityManagers = new ConcurrentHashMap<>();
        this.domainClass = domainClass;
        
        // Register the primary entity manager
        this.dataCenterEntityManagers.put("primary", primaryEntityManager);
    }

    public void registerDataCenter(String datacenter, EntityManager entityManager) {
        dataCenterEntityManagers.put(datacenter, entityManager);
    }

    private EntityManager getEntityManager(String datacenter) {
        EntityManager em = dataCenterEntityManagers.get(datacenter);
        if (em == null) {
            throw new IllegalArgumentException("Unknown datacenter: " + datacenter);
        }
        return em;
    }

    @Override
    @Transactional
    public T saveToDatacenter(T entity, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        if (!em.contains(entity)) {
            return em.merge(entity);
        }
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public T findByIdInDatacenter(ID id, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        return em.find(domainClass, id);
    }

    @Override
    @Transactional
    public void deleteFromDatacenter(T entity, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        if (!em.contains(entity)) {
            entity = em.merge(entity);
        }
        em.remove(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsInDatacenter(ID id, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        return em.find(domainClass, id) != null;
    }
} 