# Async Kafka API

This is a Spring Boot application that demonstrates asynchronous processing using Apache Kafka.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Apache Kafka 2.8 or higher

## Setup

1. Start Zookeeper and Kafka servers
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

## Testing

Send a POST request to `http://localhost:8080/api/data` with the following body:

```json
{
    "id": "1",
    "content": "Test data",
    "timestamp": "2023-11-22T10:00:00Z"
}
