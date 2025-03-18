# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-03-20

### Added
- Initial release of the Multi-Datacenter User Management System
- Multi-datacenter user data management functionality
- Debezium CDC integration for real-time data capture
- Kafka-based event distribution system
- User CRUD operations with datacenter awareness
- Optimistic locking for data consistency
- Comprehensive JavaDoc documentation
- Unit tests for core components
- README documentation with setup instructions

### Technical Details
- Implemented Domain-Driven Design architecture
- Added support for multiple PostgreSQL databases
- Integrated Debezium for Change Data Capture
- Configured Kafka producers and consumers
- Created multi-datacenter repository pattern
- Added transaction management across datacenters
- Implemented event publishing system
- Added configuration support for multiple environments

### Dependencies
- Spring Boot
- PostgreSQL
- Apache Kafka
- Debezium
- JPA/Hibernate
- Project Lombok
- JUnit 5
- Mockito 