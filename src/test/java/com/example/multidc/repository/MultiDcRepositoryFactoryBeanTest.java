package com.example.multidc.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.RepositoryMetadata;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MultiDcRepositoryFactoryBeanTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private RepositoryMetadata metadata;

    @Test
    void createRepositoryFactoryShouldReturnMultiDcRepositoryFactory() {
        // Arrange
        MultiDcRepositoryFactoryBean<TestRepository, TestEntity, Long> factoryBean = 
            new MultiDcRepositoryFactoryBean<>(TestRepository.class);

        // Act
        RepositoryFactorySupport factory = factoryBean.createRepositoryFactory(entityManager);

        // Assert
        assertTrue(factory instanceof MultiDcRepositoryFactoryBean.MultiDcRepositoryFactory);
    }

    @Test
    void getRepositoryBaseClassShouldReturnMultiDcRepositoryImpl() {
        // Arrange
        MultiDcRepositoryFactoryBean<TestRepository, TestEntity, Long> factoryBean = 
            new MultiDcRepositoryFactoryBean<>(TestRepository.class);
        MultiDcRepositoryFactoryBean.MultiDcRepositoryFactory factory = 
            (MultiDcRepositoryFactoryBean.MultiDcRepositoryFactory) factoryBean.createRepositoryFactory(entityManager);

        // Act
        Class<?> baseClass = factory.getRepositoryBaseClass(metadata);

        // Assert
        assertEquals(MultiDcRepositoryImpl.class, baseClass);
    }

    // Test classes
    private static class TestEntity {
        private Long id;
        @SuppressWarnings("unused")
        public Long getId() { return id; }
        @SuppressWarnings("unused")
        public void setId(Long id) { this.id = id; }
    }

    private interface TestRepository extends JpaRepository<TestEntity, Long> {}
} 