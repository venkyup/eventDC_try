# Multi-Datacenter User Management System

A robust Java-based system for managing user data across multiple datacenters with Change Data Capture (CDC) capabilities.

## Features

- Multi-datacenter user data management
- Real-time data replication using Debezium CDC
- Event-driven architecture with Kafka messaging
- Optimistic locking for data consistency
- Transactional operations across datacenters

## Technology Stack

- Java 17
- Spring Boot
- PostgreSQL
- Apache Kafka
- Debezium
- JPA/Hibernate

## Prerequisites

- JDK 17 or later
- Maven 3.8+
- PostgreSQL 13+
- Apache Kafka 3.0+
- Debezium 2.0+

## Configuration

### Database Setup

Configure PostgreSQL for CDC:

```sql
ALTER SYSTEM SET wal_level = logical;
ALTER SYSTEM SET max_replication_slots = 10;
ALTER SYSTEM SET max_wal_senders = 10;
```

### Application Configuration

Configure `application.yml`:

```yaml
spring:
  datasource:
    primary:
      url: jdbc:postgresql://primary-dc:5432/mydb
      username: user
      password: password
    secondary:
      url: jdbc:postgresql://secondary-dc:5432/mydb
      username: user
      password: password

debezium:
  connector:
    name: postgres-connector
    database:
      hostname: localhost
      port: 5432
      user: debezium
      password: debezium
      dbname: multidc
```

## Building

```bash
mvn clean install
```

## Running

```bash
java -Ddatacenter.id=dc1 -jar target/multidc-1.0.0.jar
```

## Architecture

The system uses:
- Domain-Driven Design principles
- Event-sourcing for data replication
- CDC for capturing database changes
- Multi-datacenter repository pattern

### Key Components

1. User Management
   - UserService: Business logic
   - UserRepository: Data access
   - UserEvents: Domain events

2. CDC Integration
   - DebeziumConnector: CDC event capture
   - KafkaEventPublisher: Event distribution

3. Multi-DC Support
   - MultiDcRepository: Cross-DC operations
   - DataCenter Configuration: Per-DC settings

## API Examples

```java
// Create user
User user = userService.createUser("username", "email@example.com");

// Update user
User updated = userService.updateUser(1L, "newUsername", "newemail@example.com");

// Delete user
userService.deleteUser(1L);
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 