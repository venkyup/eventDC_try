package com.example.multidc.infrastructure.cdc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.Instant;

/**
 * Represents a database change event captured by Change Data Capture (CDC).
 * This class maps the structure of events produced by Debezium for database changes.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseChangeEvent {
    /** Operation type: c=create, u=update, d=delete */
    private String op;
    /** Name of the database table */
    private String table;
    /** State of the record before the change */
    private Payload before;
    /** State of the record after the change */
    private Payload after;
    /** Source information about the change */
    private String source;
    /** Timestamp of the change */
    private Instant ts_ms;
    /** Transaction identifier */
    private String transaction;

    /**
     * Represents the payload structure of a database record.
     * Contains the fields of the database table that was changed.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        /** Record identifier */
        private Long id;
        /** Username field */
        private String username;
        /** Email field */
        private String email;
        /** Datacenter identifier */
        private String datacenter;
        /** Record version for optimistic locking */
        private Long version;
    }
} 