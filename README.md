# movie-ticket-booking-system
The Movie Ticket Booking System is designed to allow users to browse available movies, select showtimes, book tickets, and make payments online. The system is composed of multiple microservices, each handling different parts of the application, like movie listings, theaters, payments, notifications, and user management.

# Microservices Technologies

This repository contains an overview of key technologies used in building modern microservices architectures.

## 1. Core Spring Boot Technologies

- **Spring Boot** – Framework for building stand-alone microservices
- **Spring Web (Spring MVC/WebFlux)** – For RESTful and reactive services
- **Spring Data** – Simplifies database access (JPA, MongoDB, Redis, etc.)
- **Spring Security** – Authentication and authorization for securing microservices
- **Spring Cloud** – Tools for building distributed systems
- **Spring Cloud Stream** – Event-driven microservices with messaging systems
- **Spring Cloud Config** – Centralized external configuration management
- **Spring Cloud Netflix** – Integration with Netflix OSS components (Eureka, Hystrix, Zuul, etc.)
- **Spring Boot Actuator** – Operational insights like metrics, health checks, and tracing

## 2. API Gateway and Service Discovery

- **Spring Cloud Gateway** – Reactive API Gateway for microservices
- **Netflix Eureka** – Service registry and discovery tool
- **Consul** – Service discovery and configuration store
- **Zuul** – Netflix's older API Gateway (legacy)
- **Kong API Gateway** – Open-source API Gateway
- **Traefik** – Cloud-native reverse proxy and API Gateway
- **NGINX API Gateway** – High-performance API Gateway

## 3. Messaging & Event-Driven Architecture

- **Apache Kafka** – Distributed event streaming platform
- **RabbitMQ** – Message broker for asynchronous communication
- **ActiveMQ** – Another popular message broker
- **Spring Cloud Stream** – Abstraction for messaging platforms

## 4. Database Technologies

- **Spring Data JPA** – For relational databases (MySQL, PostgreSQL, Oracle)
- **Spring Data MongoDB** – For NoSQL databases like MongoDB
- **Redis** – In-memory data store for caching
- **Elasticsearch** – Search engine for analytics and logging
- **Cassandra** – Distributed NoSQL database

## 5. Configuration Management

- **Spring Cloud Config** – Centralized configuration across microservices
- **HashiCorp Consul** – Configuration management with key-value store
- **ZooKeeper** – Distributed configuration management

## 6. Security

- **Spring Security** – Core authentication and authorization framework
- **OAuth2 / JWT** – Token-based authentication
- **Spring Security OAuth2** – OAuth2 implementation for microservices
- **Keycloak** – Identity and access management solution
- **Auth0** – Cloud-based authentication
- **Okta** – Secure authentication for enterprises
- **Firebase Authentication** – Secure authentication for mobile and web apps

## 7. Service Communication

- **RestTemplate** – Traditional synchronous HTTP client (deprecated)
- **WebClient** – Modern reactive HTTP client
- **gRPC** – High-performance RPC framework
- **Feign** – Declarative REST client
- **Spring Cloud OpenFeign** – Enhanced Feign with Spring integration

## 8. Resilience and Fault Tolerance

- **Netflix Hystrix** – Circuit breaker (deprecated)
- **Resilience4j** – Circuit breaker, retry, rate-limiting, bulkhead pattern
- **Spring Retry** – Declarative retry mechanism

## 9. Observability & Monitoring

- **Spring Boot Actuator** – Health checks, metrics, and logs
- **Prometheus** – Monitoring and alerting
- **Grafana** – Visualization for metrics
- **ELK Stack (Elasticsearch, Logstash, Kibana)** – Log aggregation and analytics
- **Zipkin** – Distributed tracing system
- **Jaeger** – Another distributed tracing tool

## 10. Containerization & Orchestration

- **Docker** – Containerization platform
- **Kubernetes** – Orchestrates containerized applications
- **Helm** – Package manager for Kubernetes
- **Istio** – Service mesh for microservices communication

## 11. CI/CD & DevOps

- **Jenkins** – CI/CD automation tool
- **GitLab CI** – Integrated GitLab CI/CD
- **CircleCI** – Cloud-based CI/CD
- **Docker Compose** – Managing multi-container applications
- **Ansible** – Infrastructure automation tool

## 12. Testing

- **JUnit** – Unit testing framework
- **Mockito** – Mocking framework
- **Testcontainers** – Docker-based integration testing
- **Spring Boot Test** – Testing Spring Boot applications
- **WireMock** – API mocking framework
- **Pact** – Contract testing for microservices
- **Spring Cloud Contract** – API contract testing

## 13. API Documentation

- **Swagger/OpenAPI** – API documentation and testing
- **Springdoc OpenAPI** – Auto-generates OpenAPI 3 documentation

## 14. Development Tools

- **Lombok** – Reduces boilerplate Java code
- **MapStruct** – Object mapping framework
- **Postman** – API testing tool

## 15. Database Migrations

- **Flyway** – Database version control
- **Liquibase** – Database schema migration tool

## 16. GraphQL for API Development

- **Spring for GraphQL** – GraphQL integration in Spring
- **DGS Framework (Netflix)** – GraphQL framework by Netflix

## 17. API Rate Limiting & Throttling

- **Bucket4j** – Java rate limiting library
- **Redisson RateLimiter** – Redis-based rate limiting

## 18. Distributed Transactions & Sagas

- **Axon Framework** – CQRS and event-sourcing
- **Camunda / Zeebe** – Workflow engine
- **Outbox Pattern (Debezium + Kafka)** – Ensures reliable transactions

## 19. Serverless & FaaS (Functions-as-a-Service)

- **Spring Cloud Function** – Serverless Java applications
- **AWS Lambda** – Cloud-based function execution
- **Google Cloud Functions** – Google's serverless platform

## 20. Micro Frontends (React, Angular, Vue Integration)

- **Single-SPA** – Framework for micro frontends
- **Module Federation (Webpack 5)** – Federated frontend modules

## 21. Cloud-Native & Multi-Cloud Deployment

- **AWS ECS / EKS** – AWS Kubernetes and container services
- **Google Kubernetes Engine (GKE)** – Kubernetes for Google Cloud
- **Azure Kubernetes Service (AKS)** – Kubernetes for Azure

## 22. Performance Optimization & Profiling

- **JProfiler** – Java application profiling
- **VisualVM** – Open-source Java profiler
- **Async Profiler** – Low-overhead Java performance profiler

## 23. Cross-Origin Resource Sharing (CORS)

- **Spring Security CORS** – CORS protection in Spring
- **NGINX Reverse Proxy** – API Gateway-level CORS handling

