package com.example.multidc.infrastructure.cdc;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class DebeziumConnector {

    private final io.debezium.config.Configuration debeziumConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private DebeziumEngine<ChangeEvent<String, String>> engine;

    public DebeziumConnector(io.debezium.config.Configuration debeziumConfig,
                         KafkaTemplate<String, String> kafkaTemplate) {
        this.debeziumConfig = debeziumConfig;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    private void start() {
        this.engine = DebeziumEngine.create(Json.class)
            .using(debeziumConfig.asProperties())
            .notifying(record -> {
                kafkaTemplate.send(record.destination(), record.key(), record.value())
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send CDC event to Kafka", ex);
                        }
                    });
            })
            .build();

        executor.execute(engine);
    }

    @PreDestroy
    private void stop() {
        if (engine != null) {
            try {
                engine.close();
            } catch (IOException e) {
                log.error("Error stopping Debezium engine", e);
            }
        }
    }
} 