package com.example.multidc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for multi-datacenter setup.
 * Configures data sources and JPA for multiple datacenters.
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.multidc.repository",
    entityManagerFactoryRef = "multiDcEntityManagerFactory",
    transactionManagerRef = "multiDcTransactionManager"
)
public class MultiDataCenterConfiguration {

    /**
     * Creates primary datasource properties.
     *
     * @return DataSourceProperties for the primary datacenter
     */
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Creates secondary datasource properties.
     *
     * @return DataSourceProperties for the secondary datacenter
     */
    @Bean
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Creates the primary datasource.
     *
     * @return DataSource for the primary datacenter
     */
    @Primary
    @Bean
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
    }

    /**
     * Creates the secondary datasource.
     *
     * @return DataSource for the secondary datacenter
     */
    @Bean
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
    }

    /**
     * Creates the entity manager factory for multi-datacenter operations.
     *
     * @param jpaProperties JPA configuration properties
     * @return EntityManagerFactory configured for multi-datacenter support
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean multiDcEntityManagerFactory(
            JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan("com.example.multidc.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager multiDcTransactionManager(
            LocalContainerEntityManagerFactoryBean multiDcEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(multiDcEntityManagerFactory.getObject());
        return transactionManager;
    }
} 