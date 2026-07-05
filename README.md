# 📈 Investment Tracker

A microservices-based investment portfolio management system built with Spring Boot.

---

# 🚀 Overview

Investment Tracker is a backend application for managing investment portfolios, tracking market prices, calculating portfolio performance, and analyzing investment strategies.

The project is built as a microservices architecture and serves as a production-style learning project.

---

# 🏗 Architecture

Investment Tracker is built as a microservices-based application.

```text
                          API Gateway
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
 User Service         Portfolio Service     Market Data Service
        │                      │                      │
        ▼                      ▼                      ▼
 Authentication       User Positions        Stock Prices

                 ┌──────────────────────────────┐
                 │                              │
                 ▼                              ▼
        Analytics Service             Credit Service
```

---

# 📦 Microservices

| Service | Responsibility | Status |
|----------|----------------|--------|
| User Service | Authentication, authorization, user management | ✅ Completed |
| Portfolio Service | Portfolio and position management | ✅ Completed |
| Market Data Service | External market data integration | 🚧 In Progress |
| Analytics Service | Portfolio analytics and performance calculation | 📋 Planned |
| Credit Service | Loan management and leverage tracking | 📋 Planned |
| API Gateway | Routing and centralized authentication | 📋 Planned |

---

# 📊 Project Status

| Component | Status |
|-----------|:------:|
| User Service | ✅ Completed |
| Portfolio Service | ✅ Completed |
| Market Data Service | 🚧 In Progress |
| Analytics Service | 📋 Planned |
| Credit Service | 📋 Planned |
| Infrastructure | 🚧 In Progress |
| Documentation | 🚧 In Progress |

---

# 🛠 Technology Stack

## Backend

- Java 23
- Spring Boot
- Spring Security
- Maven

## Database

- PostgreSQL
- Flyway

## Testing

- JUnit 5
- Mockito
- WireMock
- H2 Database

## Documentation

- Swagger / OpenAPI

## DevOps

- Docker
- GitHub
- Jira

## Future

- Redis
- Resilience4j
- GitHub Actions
- API Gateway
