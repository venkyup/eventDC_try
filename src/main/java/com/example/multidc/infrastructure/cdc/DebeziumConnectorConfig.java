package com.example.multidc.infrastructure.cdc;

import io.debezium.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DebeziumConnectorConfig {

    @Value("${debezium.connector.name}")
    private String connectorName;

    @Value("${debezium.connector.database.hostname}")
    private String databaseHost;

    @Value("${debezium.connector.database.port}")
    private String databasePort;

    @Value("${debezium.connector.database.user}")
    private String databaseUser;

    @Value("${debezium.connector.database.password}")
    private String databasePassword;

    @Value("${debezium.connector.database.dbname}")
    private String databaseName;

    @Value("${debezium.connector.database.server.name}")
    private String serverName;

    @Value("${debezium.connector.schema.include.list}")
    private String schemaIncludeList;

    @Value("${debezium.connector.table.include.list}")
    private String tableIncludeList;

    @Bean
    public Configuration debeziumConfig() {
        return Configuration.create()
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("name", connectorName)
            .with("database.hostname", databaseHost)
            .with("database.port", databasePort)
            .with("database.user", databaseUser)
            .with("database.password", databasePassword)
            .with("database.dbname", databaseName)
            .with("database.server.name", serverName)
            .with("schema.include.list", schemaIncludeList)
            .with("table.include.list", tableIncludeList)
            .with("plugin.name", "pgoutput")
            .with("slot.name", connectorName + "_slot")
            .build();
    }
} 