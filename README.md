# 📈 Investment Tracker

A microservices-based investment portfolio management system built with Java and Spring Boot.

## 🚀 Overview

Investment Tracker is a backend application designed to manage investment portfolios, record buy and sell transactions, track market prices, calculate portfolio performance, and analyze investments.

The project follows a microservices architecture and is developed as a production-style learning project, focusing on clean architecture, maintainability, security, testing, and real-world backend development practices.

### Key Features

- User registration, authentication, and authorization
- JWT-based authentication through a centralized API Gateway
- Investment portfolio and position management
- BUY and SELL transaction processing
- Automatic position quantity and average purchase price updates
- Market price retrieval and integration
- Portfolio analytics and performance calculations
- Credit and loan management
- Database migrations and API documentation
- Containerization with Docker

---

## 🏗 Architecture

Investment Tracker consists of six backend microservices connected through an API Gateway.

```text
                         Client / Frontend
                                |
                                v
                       +------------------+
                       |   API Gateway    |
                       |------------------|
                       | Request Routing  |
                       | JWT Validation   |
                       | CORS Policy      |
                       +------------------+
                                |
          +---------------------+---------------------+
          |                     |                     |
          v                     v                     v
   +--------------+     +----------------+    +----------------+
   | User Service |     | Portfolio      |    | Market Data    |
   |              |     | Service        |    | Service        |
   +--------------+     +----------------+    +----------------+
          |                     |                     |
          |                     |                     |
          v                     v                     v
   Authentication        Positions &            Market Prices
   Authorization         Transactions           External Data
   User Management
                               
          +---------------------+---------------------+
          |                                           |
          v                                           v
   +----------------+                        +----------------+
   | Analytics      |                        | Credit         |
   | Service        |                        | Service        |
   +----------------+                        +----------------+
          |                                           |
          v                                           v
   Portfolio Analytics                        Loan Management
   Performance Metrics                        Credit Payments
```

The API Gateway serves as the main entry point for client requests. Each microservice is responsible for a specific business domain.

### API Gateway Routes

| Path | Target Service |
|------|----------------|
| `/auth/**` | User Service |
| `/users/**` | User Service |
| `/admin/**` | User Service |
| `/positions/**` | Portfolio Service |
| `/prices/**` | Market Data Service |
| `/credits/**` | Credit Service |
| `/credit-payments/**` | Credit Service |
| `/dashboard` and `/dashboard/**` | Analytics Service |

---

## 📦 Microservices

### 1. User Service

Responsible for user identity and access management.

**Features:**
- User registration and login
- JWT authentication
- User management
- Authorization and access control

**Status:** ✅ Completed

### 2. Portfolio Service

Responsible for managing investment portfolios, positions, and transactions.

**Features:**
- Position management
- BUY transaction processing
- SELL transaction processing
- Position quantity updates
- Weighted average purchase price calculation
- Transaction-based portfolio updates
- Position filtering and querying
- Validation of available quantities before selling

Transactions represent investment operations, while positions represent the current aggregated holdings.

**Status:** ✅ Completed

### 3. Market Data Service

Responsible for retrieving and providing financial market data.

**Features:**
- External market data integration
- Stock price retrieval
- Market price API endpoints

**Status:** ✅ Completed

### 4. Analytics Service

Responsible for investment portfolio analytics.

**Features:**
- Portfolio analytics
- Investment performance calculations
- Dashboard data aggregation

**Status:** ✅ Completed

### 5. Credit Service

Responsible for managing loans and credit-related information.

**Features:**
- Credit management
- Credit payment management
- Financial obligations tracking

**Status:** ✅ Completed

### 6. API Gateway

Provides a centralized entry point for backend services.

**Features:**
- Request routing
- JWT token validation
- Centralized authentication enforcement
- Public endpoint handling
- CORS configuration
- Gateway error handling

Built with Spring Cloud Gateway WebFlux.

**Status:** ✅ Completed

---

## 🔄 Portfolio Transaction Processing

Portfolio Service supports BUY and SELL operations.

### BUY Transaction

When a BUY transaction is processed:

1. The transaction data is validated.
2. A transaction record is created.
3. The corresponding position is found or created.
4. The position quantity is increased.
5. The weighted average purchase price is recalculated.
6. The updated position is persisted.

### SELL Transaction

When a SELL transaction is processed:

1. The transaction data is validated.
2. The available position quantity is checked.
3. Selling more than the available quantity is rejected.
4. A transaction record is created.
5. The position quantity is decreased.
6. The existing average purchase price remains unchanged.

This design separates the history of investment operations from the current portfolio state.

---

## 🛠 Technology Stack

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Cloud Gateway WebFlux
- Spring Data JPA
- Maven

### Database

- PostgreSQL
- Flyway

### Testing

- JUnit 5
- Mockito
- WireMock
- H2 Database

### API Documentation

- Swagger
- OpenAPI

### Infrastructure and DevOps

- Docker
- Git
- GitHub
- Jira

---

## 🔐 Security

Investment Tracker uses JWT-based authentication.

The User Service handles authentication and token issuance.

The API Gateway validates JWT tokens for protected routes and allows explicitly configured public endpoints.

### Public Endpoints

| Method | Endpoint |
|--------|----------|
| POST | `/auth/login` |
| POST | `/auth/register` |
| GET | `/actuator/health` |

### CORS

The API Gateway provides centralized CORS configuration.

The allowed frontend origin is configurable through the `FRONTEND_ORIGIN` property.

Supported HTTP methods:

- GET
- POST
- PUT
- PATCH
- DELETE
- OPTIONS

Allowed request headers include `Authorization` and `Content-Type`.

CORS preflight requests are handled without requiring a JWT token.

---

## 🗄 Database Management

Investment Tracker uses PostgreSQL for persistent storage.

Flyway is used to manage database schema migrations, ensuring that database changes are versioned and reproducible.

Database configuration is managed separately for each applicable microservice.

---

## 🧪 Testing

The project uses automated testing to verify business logic and service behavior.

Testing technologies include:

- **JUnit 5** for automated tests
- **Mockito** for mocking dependencies
- **WireMock** for HTTP dependency simulation
- **H2** for applicable database tests

The testing approach includes unit and integration tests where implemented.

API Gateway CORS behavior has also been verified manually using HTTP requests.

---

## ⚙️ Configuration

Application configuration is managed using Spring Boot YAML configuration files and environment variables.

### API Gateway

The API Gateway runs on port `8086` in the current local configuration.

The following environment variables are used:

| Variable | Description |
|----------|-------------|
| `USER_SERVICE_URL` | User Service URL |
| `PORTFOLIO_SERVICE_URL` | Portfolio Service URL |
| `MARKET_DATA_SERVICE_URL` | Market Data Service URL |
| `ANALYTICS_SERVICE_URL` | Analytics Service URL |
| `CREDIT_SERVICE_URL` | Credit Service URL |
| `INVESTMENT_TRACKER_JWT_SECRET` | JWT validation secret |
| `FRONTEND_ORIGIN` | Allowed frontend origin for CORS |

Example local service URLs:

```yaml
USER_SERVICE_URL: http://localhost:8080
PORTFOLIO_SERVICE_URL: http://localhost:8081
MARKET_DATA_SERVICE_URL: http://localhost:8082
CREDIT_SERVICE_URL: http://localhost:8083
ANALYTICS_SERVICE_URL: http://localhost:8087

FRONTEND_ORIGIN: http://localhost:3000
```

Secrets and environment-specific credentials should not be committed to version control.

---

## 📊 Project Status

| Component | Status |
|-----------|--------|
| User Service | ✅ Completed |
| Portfolio Service | ✅ Completed |
| BUY/SELL Transactions | ✅ Completed |
| Market Data Service | ✅ Completed |
| Analytics Service | ✅ Completed |
| Credit Service | ✅ Completed |
| API Gateway | ✅ Completed |
| Infrastructure | 🚧 Ongoing Improvements |
| Documentation | 🚧 Ongoing Improvements |

All six core backend microservices have been implemented.

Further development focuses on extending functionality, improving infrastructure, and increasing production readiness.

---

## 🗺 Roadmap

Potential future improvements include:

- Redis caching
- Resilience4j for fault tolerance
- GitHub Actions CI/CD pipelines
- Additional automated integration tests
- Improved observability and monitoring
- Extended portfolio analytics
- Further transaction and portfolio features
- Frontend integration

These are planned improvements rather than completed features.

---

## 🎯 Project Goals

Investment Tracker is designed to provide practical experience with:

- Microservices architecture
- REST API design
- Spring Boot and Spring Security
- Transactional business logic
- Database modeling and migrations
- Authentication and authorization
- API Gateway patterns
- Automated testing
- Docker-based development
- Production-oriented software engineering

The long-term goal is to evolve Investment Tracker into a maintainable, extensible investment management platform.

---

## 📄 License

License information has not yet been specified.
