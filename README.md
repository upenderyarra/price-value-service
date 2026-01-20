# Last Value Price Service  
Spring Boot | In-Memory | Batch Price Processing

## Overview
This project implements an in-memory Last Value Price Service as part of the S&P Global developer test.

The goal of the service is to allow producers to publish price data in batch runs and consumers to retrieve the most recent price for a financial instrument, determined by the `asOf` timestamp.

The solution focuses on clean design, correctness, thread safety, and maintainability.

## Business Requirements Covered
- Producers can start a batch run, upload price records in chunks, and complete or cancel a batch
- Consumers can retrieve the last price for a given instrument
- Prices become visible only after batch completion
- Cancelled batches are discarded
- Resilient to incorrect producer call order and concurrent access
- In-memory implementation (no database)

## Technology Stack
- Java 17
- Spring Boot 3.x
- Maven
- JUnit 5
- IntelliJ HTTP Client

## High-Level Design
- Clear service interfaces for producer and consumer APIs
- In-memory storage using ConcurrentHashMap
- Atomic visibility of prices on batch completion
- Last price selection based on `asOf` timestamp

## Batch Processing Flow
1. Start batch
2. Upload price records
3. Complete or cancel batch
4. On completion, prices become atomically visible
5. On cancellation, batch data is discarded

## REST API Endpoints

### Producer APIs
POST /prices/batch/start  
POST /prices/batch/{batchId}/upload  
POST /prices/batch/{batchId}/complete  
POST /prices/batch/{batchId}/cancel  

### Consumer API
GET /prices/{instrumentId}

## Running the Application
mvn clean test  
mvn spring-boot:run  

## API Testing (Without Postman)
This project includes an IntelliJ HTTP client file:
http/price-service.http

Important:
Execute the HTTP file using **Run All** to preserve request execution context and variable sharing.

## Possible Improvements
- Database persistence
- Validation and error handling
- Monitoring and metrics
- Kafka integration

## Summary
This solution meets all functional and technical requirements while maintaining production-quality standards.
