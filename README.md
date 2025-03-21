# Reconciliation System

## Project Overview
This is a robust reconciliation system designed to handle large-scale transaction processing and reconciliation. The system is built using Spring Boot and incorporates modern technologies to ensure scalability, reliability, and data consistency.

### Key Features
- Transaction creation and management
- File upload processing for reconciliation
- Real-time transaction status tracking
- Scalable message processing using Kafka
- ACID-compliant data storage using PostgreSQL
- Docker containerization for easy deployment

## Technology Stack
- **Spring Boot**: Core framework for building the application
- **Apache Kafka**: Used for scalable message processing and event streaming
  - Enables asynchronous processing of large datasets
  - Provides fault tolerance and high throughput
  - Handles back-pressure effectively
- **PostgreSQL**: Primary database for transaction storage
  - ACID compliance ensures data integrity
  - Robust transaction management
  - Efficient handling of complex queries and relationships

## Prerequisites
- Docker Desktop (Mac/Windows) or Colima (Mac)
- Java 17 or higher
- Maven or Gradle

## Getting Started

### 1. Environment Setup
Create a `.env` file in the root directory:
```bash
touch .env
```

Add the following configuration to the `.env` file:
```properties
# Database Configuration
POSTGRES_DB=reconciliation_db
POSTGRES_USER=reconciliation_user
POSTGRES_PASSWORD=password

# Kafka Configuration
KAFKA_BROKER_ID=1
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1

# Application Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/reconciliation_db
SPRING_DATASOURCE_USERNAME=reconciliation_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29092
SPRING_KAFKA_CONSUMER_GROUP_ID=reconciliation-group
KAFKA_TOPIC_SETTLEMENTS=settlements
KAFKA_TOPIC_PAYOUTS=payouts
```

### 2. Running the Application
1. Start Docker Desktop or Colima
2. Build and run the application using Docker Compose:
```bash
docker-compose up --build
```

## API Documentation

### 1. Create Transaction
```bash
curl --location 'http://localhost:8080/api/transactions' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC' \
--data '{
    "userId": "user124",
    "amount": 8000.00,
    "status": "FAILED",
    "userBankAccountId": "ACC125",
    "partnerBankAccountId": "ACC458"
}'
```
Response:
```json
{
    "status": "FAILED",
    "message": "Transaction created successfully",
    "transactionId": "c057fe4c-5bed-4870-b015-677e9e26a5ee"
}
```
### 2. Get Transactions Based on status
```bash
curl --location 'http://localhost:8080/api/transactions/status/{status}' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC'
```
Response:
```json
[
    {
        "transactionId": "62fd620f-e779-4451-bfc7-bd3b9aef05cc",
        "userId": "user123",
        "amount": 1000.00,
        "status": "SUCCESS",
        "userBankAccountId": "ACC123",
        "partnerBankAccountId": "ACC456",
        "createdAt": "2025-03-21T15:31:07.003919",
        "updatedAt": "2025-03-21T15:31:07.003948"
    },
    {
        "transactionId": "763f33fa-4b10-4295-afc7-4fee5158c898",
        "userId": "user124",
        "amount": 3000.00,
        "status": "SUCCESS",
        "userBankAccountId": "ACC124",
        "partnerBankAccountId": "ACC457",
        "createdAt": "2025-03-21T15:31:24.943022",
        "updatedAt": "2025-03-21T15:31:24.943047"
    }
]
```

### 3. Upload Reconciliation File
```bash
curl --location 'http://localhost:8080/api/reconciliation/upload' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC' \
--form 'file=@"/Users/user/Downloads/21_03_2025 - Sheet1.csv"'
```

Sample CSV format:
```csv
transactionId,userId,amount,userBankAccountId,partnerBankAccountId
68946074-9cf6-4bee-af3a-307f86265cab,user123,1000,ACC123,ACC456
```

Response:
```json
{
  "batchId": "batch-789012",
  "message": "File processed successfully"
}
```

### 4. API to Get the statistics of number of records uploaded and amount received
```bash
curl --location 'http://localhost:8080/api/reconciliation/statistics/{batchId}' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC'
```

Response:
```json
{
    "batchId": "21-03-2025.csv_2025-03-21T14:50:58.249547",
    "totalRecords": 5,
    "totalAmountReceived": 15000.00,
    "settledCount": 2
}
```


### 5. API to Get the Summary of total Reconciled and Non Reconciled amount 
```bash
curl --location 'http://localhost:8080/api/reconciliation/summary/{batchId}' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC'
```

Response:
```json
{
    "batchId": "21-03-2025.csv_2025-03-21T14:50:58.249547",
    "totalReconciled": 3000.00,
    "totalNonReconciled": 12000.00
}
```



### 6. API to Get the summary of agent wise payout records
```bash
curl --location 'http://localhost:8080/api/reconciliation/payout-summary/{batchId}' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC'
```

Response:
```json
[
    {
        "partnerBankAccountId": "ACC456",
        "totalAmountPaid": 1000.00
    },
    {
        "partnerBankAccountId": "ACC457",
        "totalAmountPaid": 2000.00
    }
]
```


### 7. API to Get settlements based on status
```bash
curl --location 'http://localhost:8080/api/settlements/status/{status}' \
--header 'Cookie: JSESSIONID=6583D086EBDCBDE23C0CE7E7AB6062AC'
```

Response:
```json
[
    {
        "settlementId": "c1082507-17c1-4d73-a6de-57e28109ba66",
        "transactionId": "14628b03-0e6c-43d7-8042-31c8c734482c",
        "settledAmount": 3000.00,
        "userId": "user125",
        "userBankAccountId": "ACC125",
        "partnerBankAccountId": "ACC458",
        "status": "FAILED",
        "settledAt": "2025-03-21T14:50:58.301758",
        "batchId": "21-03-2025.csv_2025-03-21T14:50:58.249547",
        "comments": "Validation failed: Status mismatch, amount mismatch, or bank account mismatch."
    },
    {
        "settlementId": "37e75771-1825-4d71-8be1-2f77e0af574b",
        "transactionId": "TRX004",
        "settledAmount": 4000.00,
        "userId": "user126",
        "userBankAccountId": "ACC126",
        "partnerBankAccountId": "ACC459",
        "status": "FAILED",
        "settledAt": "2025-03-21T14:50:58.301878",
        "batchId": "21-03-2025.csv_2025-03-21T14:50:58.249547",
        "comments": null
    },
    {
        "settlementId": "adeb9ba6-0611-4aee-9a73-902388b7c46e",
        "transactionId": "TRX005",
        "settledAmount": 5000.00,
        "userId": "user127",
        "userBankAccountId": "ACC127",
        "partnerBankAccountId": "ACC460",
        "status": "FAILED",
        "settledAt": "2025-03-21T14:50:58.302009",
        "batchId": "21-03-2025.csv_2025-03-21T14:50:58.249547",
        "comments": null
    }
]
```

## Important Notes
1. Always create a transaction first and use the returned `transactionId` when uploading reconciliation files
2.Cross check all ports 8080,5433,9092,2181 are available
3. Ensure the CSV file format matches the provided sample
4. The system uses Kafka for asynchronous processing, so there might be a slight delay in reconciliation updates

## Troubleshooting
- If containers fail to start, ensure no other services are using the required ports (5432, 9092, 2181)
- Check Docker logs using `docker-compose logs -f [service-name]`
- Verify the `.env` file is properly configured
- Ensure sufficient system resources are available 
-
