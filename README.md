# 📈 Investment Tracker

A microservices-based investment portfolio management system built with Spring Boot.

---

# 🚀 Overview

Investment Tracker is a backend application for managing investment portfolios, tracking market prices, recording portfolio transactions, managing credits, and calculating portfolio analytics.

The project is built using a microservices architecture and serves as a production-style learning project.

The backend currently consists of six independent Spring Boot services connected through an API Gateway.

---

# 🏗 Architecture

Investment Tracker is built as a microservices-based application.

```text
                              API Gateway
                                  │
          ┌───────────────────────┼────────────────────────┐
          │                       │                        │
          ▼                       ▼                        ▼
   User Service          Portfolio Service       Market Data Service
          │                       │
          ▼                       ▼
   PostgreSQL              PostgreSQL
                                  │
                     ┌────────────┴────────────┐
                     │                         │
                     ▼                         ▼
             Analytics Service          Credit Service
                                             │
                                             ▼
                                         PostgreSQL
```

The API Gateway provides a single entry point to the backend platform and routes requests to the corresponding services.

The services remain independently deployable applications with their own configuration, tests, and build artifacts.

---

# 📦 Microservices

| Service | Port | Responsibility | Status |
|----------|:----:|----------------|:------:|
| User Service | 8080 | Authentication, authorization and user management | ✅ Completed |
| Portfolio Service | 8081 | Positions and portfolio transactions | ✅ Completed |
| Market Data Service | 8082 | External market data integration | ✅ Completed |
| Credit Service | 8083 | Credit and payment management | ✅ Completed |
| API Gateway | 8086 | Routing and centralized JWT authentication | ✅ Completed |
| Analytics Service | 8087 | Portfolio analytics and performance calculation | ✅ Completed |

---

# 📊 Project Status

The backend platform currently provides the core functionality required for portfolio management and analytics.

| Component | Status |
|-----------|:------:|
| User Management & Authentication | ✅ Completed |
| Portfolio Management | ✅ Completed |
| Portfolio Transactions | ✅ Completed |
| Market Data Integration | ✅ Completed |
| Credit Management | ✅ Completed |
| Portfolio Analytics | ✅ Completed |
| API Gateway | ✅ Completed |
| Local Runtime Configuration | ✅ Completed |
| Docker Compose Infrastructure | ✅ Completed |
| Container Health Checks | ✅ Completed |
| Local Development Experience | 🚧 In Progress |
| Frontend | 📋 Planned |
| Test Environment & Deployment | 📋 Planned |
| End-to-End Testing | 📋 Planned |

---

# 🛠 Technology Stack

## Backend

- Java 23
- Spring Boot
- Spring Security
- Spring WebFlux
- Maven

## Database

- PostgreSQL
- Flyway
- Spring Data JPA / Hibernate

## Testing

- JUnit 5
- Mockito
- WireMock
- H2 Database

## API & Documentation

- REST
- Swagger / OpenAPI
- JWT Authentication

## DevOps

- Docker
- Docker Compose
- Maven Multi-Module Build
- Git
- GitHub
- Jira

---

# 💻 Local Development

The complete backend platform can be built and started locally using Maven and Docker Compose.

The local platform consists of:

- 6 Spring Boot services
- 3 PostgreSQL databases
- 9 Docker containers in total

---

## Prerequisites

Make sure the following tools are installed before starting the platform:

- Java 23
- Maven
- Docker
- Docker Compose
- Git

Verify the installations:

```bash
java -version
mvn -version
docker --version
docker compose version
git --version
```

---

## Clone the Repository

Clone the repository and enter the project directory:

```bash
git clone <repository-url>
cd investment-tracker
```

---

## Configuration

Local secrets and external API credentials are provided through environment variables.

The Docker Compose configuration expects a `.env` file containing the required secrets.

Create:

```text
infra/.env
```

and provide the required variables:

```env
INVESTMENT_TRACKER_JWT_SECRET=<your-jwt-secret>
ALPHA_VANTAGE_API_KEY=<your-alpha-vantage-api-key>
FINNHUB_API_KEY=<your-finnhub-api-key>
```

Do not commit real secrets to Git.

The `.env` file must remain ignored by Git.

Database configuration required by the services is provided by Docker Compose.

---

## Build the Backend

Investment Tracker uses a root Maven aggregator project.

From the repository root, build all backend services with:

```bash
mvn clean package
```

Maven Reactor builds all six backend modules as part of a single build.

The individual services can still be built independently from their own directories when necessary.

---

## Docker Images

After changing application code or resources, rebuild the corresponding Docker images before recreating the containers.

Each backend service has its own Docker image.

Example:

```bash
docker build -t investment-tracker-user-service:0.8.0 services/user-service
```

The same approach applies to the other backend services.

If only `docker-compose.yml` changes and the application JARs/images remain unchanged, rebuilding the images is not required.

---

## Start the Platform

Docker Compose configuration is located in:

```text
infra/docker-compose.yml
```

Move to the infrastructure directory:

```bash
cd infra
```

Start the complete platform in detached mode:

```bash
docker compose up -d
```

Docker Compose starts the PostgreSQL databases and backend services.

Services that depend on PostgreSQL wait for their databases to become healthy before starting.

Application health checks use Spring Boot Actuator.

---

## Verify the Platform

Check the state of all containers:

```bash
docker compose ps
```

The local platform consists of nine containers:

```text
3 PostgreSQL containers
6 Spring Boot containers
```

After startup, all containers should eventually report:

```text
healthy
```

A container being `running` does not necessarily mean that the application inside it is ready. Docker health checks are used to determine application readiness.

---

## Health Checks

Spring Boot services expose an Actuator health endpoint:

```text
/actuator/health
```

For example, the API Gateway can be checked locally with:

```bash
curl http://localhost:8086/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

The PostgreSQL containers use `pg_isready` for their Docker health checks.

---

## Local Ports

### Backend Services

| Service | Port |
|---------|:----:|
| User Service | 8080 |
| Portfolio Service | 8081 |
| Market Data Service | 8082 |
| Credit Service | 8083 |
| API Gateway | 8086 |
| Analytics Service | 8087 |

### PostgreSQL

| Database | Host Port |
|----------|:---------:|
| Portfolio Database | 5433 |
| Credit Database | 5434 |
| User Database | 5435 |

These ports are exposed to the host for local development and debugging.

Inside Docker Compose, containers communicate using Docker service names and container ports rather than `localhost`.

---

## Stop the Platform

To stop and remove the containers:

```bash
docker compose down
```

Database data is stored in Docker named volumes and therefore survives normal container recreation.

To inspect the platform before stopping it:

```bash
docker compose ps
```

---

# 🧱 Repository Structure

```text
investment-tracker/
│
├── pom.xml
├── README.md
│
├── services/
│   ├── user-service/
│   ├── portfolio-service/
│   ├── market-data-service/
│   ├── credit-service/
│   ├── analytics-service/
│   └── api-gateway/
│
└── infra/
    ├── docker-compose.yml
    └── .env
```

The root `pom.xml` acts as a Maven aggregator and allows all backend services to be built from the repository root.

The `infra` directory contains infrastructure required for running the platform locally.

---

# 🔄 Typical Development Workflow

A typical local development cycle is:

```text
Change application code
        │
        ▼
mvn clean package
        │
        ▼
Build affected Docker image
        │
        ▼
docker compose up -d
        │
        ▼
docker compose ps
        │
        ▼
Containers healthy
```

If only Docker Compose configuration changes, rebuilding the application and Docker images may not be necessary.

---

# 🗺 Roadmap

Planned development includes:

- Frontend MVP
- Portfolio Management UI
- Test Environment & Deployment
- Playwright End-to-End Tests
- Additional market data providers
- Resilience and fallback improvements
- Exchange rate support
- Refresh tokens and logout
- AI Advisor Service
- Notification Service

---

# 🎯 Project Goal

The goal of Investment Tracker is to build a production-style investment management platform while learning and applying modern backend development practices.

The project focuses on:

- Microservices architecture
- REST API design
- Authentication and authorization
- Database design and migrations
- External API integration
- Automated testing
- Containerization
- Local development infrastructure
- Observability and health checks
- Frontend integration
- CI/CD and deployment

- Redis
- Resilience4j
- GitHub Actions
- API Gateway
