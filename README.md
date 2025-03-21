# Reconciliation Application

A Spring Boot application for handling transaction reconciliation with Kafka and PostgreSQL.

## Prerequisites

- Docker
- Docker Compose

## Setup and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd ReconciliationApplication
```

2. Build and run using Docker Compose:
```bash
docker-compose up --build
```

This will start:
- Spring Boot application on port 8080
- PostgreSQL database on port 5432
- Kafka on port 9092
- Zookeeper on port 2181

## API Endpoints

### Transaction APIs
- Create Transaction: `POST /api/transactions`
- Get Transactions by Status: `GET /api/transactions/status/{status}`

### Reconciliation APIs
- Upload Settlement File: `POST /api/reconciliation/upload`

### Report APIs
- Get Batch Statistics: `GET /api/reconciliation/statistics/{batchId}`
- Get Reconciliation Summary: `GET /api/reconciliation/summary/{batchId}`
- Get Agent Payout Summary: `GET /api/reconciliation/payout-summary/{batchId}`

## Testing

To run tests locally:
```bash
./gradlew test
```

## Development

The application uses:
- Java 21
- Spring Boot
- PostgreSQL
- Apache Kafka
- Gradle

## Environment Variables

Key environment variables:
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka bootstrap servers
- `KAFKA_TOPIC_SETTLEMENTS`: Kafka topic for settlements
- `KAFKA_TOPIC_PAYOUTS`: Kafka topic for payouts

## Troubleshooting

1. If the application fails to start, check if all required ports are available
2. Ensure Docker and Docker Compose are running
3. Check logs using `docker-compose logs -f app`
4. For database issues, check PostgreSQL logs: `docker-compose logs -f postgres`
5. For Kafka issues, check Kafka logs: `docker-compose logs -f kafka` 