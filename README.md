# 📈 Investment Tracker

A microservices-based investment portfolio management system built with Spring Boot.

---

## 🚀 Overview

Investment Tracker is a backend application for managing investment portfolios, tracking market prices, calculating portfolio performance, and analyzing investment strategies.

The project is built as a microservices architecture and serves as a production-style learning project.

---
## 📊 Project Status

| Component | Status |
|-----------|:------:|
| User Service | 🚧 In Progress |
| Portfolio Service | 🚧 In Progress |
| Market Data Service | 🚧 In Progress |
| Analytics Service | 📋 Planned |
| Credit Service | 📋 Planned |
| Infrastructure | 🚧 In Progress |
| Documentation | 🚧 In Progress |
## 🛠️ Technology Stack

### Backend

- Java 23
- Spring Boot
- Maven

### Database

- PostgreSQL

### Testing

- JUnit 5
- WireMock
- Mockito

### DevOps

- Docker
- GitHub
- Jira

### Future

- Redis
- Resilience4j
- GitHub Actions

- # 🏗 Architecture

Investment Tracker is built as a microservices-based application.

```text
                API Gateway
                     │
 ┌───────────────────┼───────────────────┐
 │                   │                   │
 ▼                   ▼                   ▼
User Service   Portfolio Service   Market Data Service
 │                   │                   │
 ▼                   ▼                   ▼
Authentication   User Positions   Stock Prices

         ┌─────────────────────────┐
         ▼                         ▼
 Analytics Service         Credit Service
