package com.example.multidc.repository;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

public class MultiDcRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, ID> {

  public MultiDcRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  @NonNull
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new MultiDcRepositoryFactory(entityManager);
  }

  private static class MultiDcRepositoryFactory extends JpaRepositoryFactory {
    public MultiDcRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    @NonNull
    protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
      return MultiDcRepositoryImpl.class;
    }

    @Override
    @NonNull
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(
        @NonNull RepositoryInformation information, @NonNull EntityManager entityManager) {
      Class<?> domainClass = information.getDomainType();
      return new MultiDcRepositoryImpl<>(domainClass, entityManager);
    }
  }
} 