package com.example.multidc.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MultiDcRepositoryImplTest {

    @Mock
    private EntityManager primaryEntityManager;

    @Mock
    private EntityManager secondaryEntityManager;

    private MultiDcRepositoryImpl<TestEntity, Long> repository;

    @BeforeEach
    void setUp() {
        repository = new MultiDcRepositoryImpl<>(TestEntity.class, primaryEntityManager);
    }

    @Test
    void constructorShouldRegisterPrimaryEntityManager() {
        // Act & Assert
        assertDoesNotThrow(() -> repository.saveToDatacenter(new TestEntity(), "primary"));
    }

    @Test
    void registerDataCenterShouldAddNewEntityManager() {
        // Act
        repository.registerDataCenter("secondary", secondaryEntityManager);

        // Assert
        assertDoesNotThrow(() -> repository.saveToDatacenter(new TestEntity(), "secondary"));
    }

    @Test
    void saveToDatacenterShouldMergeNewEntity() {
        // Arrange
        TestEntity entity = new TestEntity();
        when(primaryEntityManager.contains(entity)).thenReturn(false);
        when(primaryEntityManager.merge(entity)).thenReturn(entity);

        // Act
        TestEntity result = repository.saveToDatacenter(entity, "primary");

        // Assert
        verify(primaryEntityManager).merge(entity);
        assertEquals(entity, result);
    }

    @Test
    void saveToDatacenterShouldPersistExistingEntity() {
        // Arrange
        TestEntity entity = new TestEntity();
        when(primaryEntityManager.contains(entity)).thenReturn(true);

        // Act
        TestEntity result = repository.saveToDatacenter(entity, "primary");

        // Assert
        verify(primaryEntityManager).persist(entity);
        assertEquals(entity, result);
    }

    @Test
    void findByIdInDatacenterShouldReturnEntity() {
        // Arrange
        TestEntity entity = new TestEntity();
        when(primaryEntityManager.find(TestEntity.class, 1L)).thenReturn(entity);

        // Act
        TestEntity result = repository.findByIdInDatacenter(1L, "primary");

        // Assert
        assertEquals(entity, result);
    }

    @Test
    void deleteFromDatacenterShouldMergeAndRemove() {
        // Arrange
        TestEntity entity = new TestEntity();
        when(primaryEntityManager.contains(entity)).thenReturn(false);
        when(primaryEntityManager.merge(entity)).thenReturn(entity);

        // Act
        repository.deleteFromDatacenter(entity, "primary");

        // Assert
        verify(primaryEntityManager).merge(entity);
        verify(primaryEntityManager).remove(entity);
    }

    @Test
    void deleteFromDatacenterShouldRemoveExistingEntity() {
        // Arrange
        TestEntity entity = new TestEntity();
        when(primaryEntityManager.contains(entity)).thenReturn(true);

        // Act
        repository.deleteFromDatacenter(entity, "primary");

        // Assert
        verify(primaryEntityManager, never()).merge(entity);
        verify(primaryEntityManager).remove(entity);
    }

    @Test
    void existsInDatacenterShouldReturnTrue() {
        // Arrange
        when(primaryEntityManager.find(TestEntity.class, 1L)).thenReturn(new TestEntity());

        // Act
        boolean result = repository.existsInDatacenter(1L, "primary");

        // Assert
        assertTrue(result);
    }

    @Test
    void existsInDatacenterShouldReturnFalse() {
        // Arrange
        when(primaryEntityManager.find(TestEntity.class, 1L)).thenReturn(null);

        // Act
        boolean result = repository.existsInDatacenter(1L, "primary");

        // Assert
        assertFalse(result);
    }

    @Test
    void getEntityManagerShouldThrowExceptionForUnknownDatacenter() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> repository.saveToDatacenter(new TestEntity(), "unknown"));
    }

    // Test Entity class
    private static class TestEntity {
        private Long id;
        @SuppressWarnings("unused")
        public Long getId() { return id; }
        @SuppressWarnings("unused")
        public void setId(Long id) { this.id = id; }
    }
} 