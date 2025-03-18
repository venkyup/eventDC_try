package com.example.multidc.infrastructure.cdc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseChangeEvent {
    private String op; // c=create, u=update, d=delete
    private String table;
    private Payload before;
    private Payload after;
    private String source;
    private Instant ts_ms;
    private String transaction;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        private Long id;
        private String username;
        private String email;
        private String datacenter;
        private Long version;
    }
} 