package com.example.multidc.infrastructure.persistence;

import com.example.multidc.domain.user.User;
import com.example.multidc.domain.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JpaUserRepository extends SimpleJpaRepository<User, Long> implements UserRepository {
    
    private final Map<String, EntityManager> dataCenterEntityManagers;

    public JpaUserRepository(EntityManager primaryEntityManager) {
        super(User.class, primaryEntityManager);
        this.dataCenterEntityManagers = new ConcurrentHashMap<>();
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
    @Transactional(readOnly = true)
    public Optional<User> findByIdAndDatacenter(Long id, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    @Transactional
    public User save(User user, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        if (!em.contains(user)) {
            return em.merge(user);
        }
        em.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(User user, String datacenter) {
        EntityManager em = getEntityManager(datacenter);
        if (!em.contains(user)) {
            user = em.merge(user);
        }
        em.remove(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id, String datacenter) {
        return findByIdAndDatacenter(id, datacenter).isPresent();
    }
} 