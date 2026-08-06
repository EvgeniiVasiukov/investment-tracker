# Analytics Service

## Overview

Analytics Service is responsible for aggregating investment data from multiple microservices and calculating dashboard metrics for the Investment Tracker platform.

The service does not store investment data. Instead, it collects information from other services, performs business calculations, and returns a unified dashboard response.

---

## Responsibilities

The Analytics Service is responsible for:

- Aggregating portfolio data.
- Retrieving current market prices.
- Retrieving current credit information.
- Calculating dashboard metrics.
- Returning a unified dashboard response.

The service does **not**:

- manage users;
- execute buy/sell operations;
- store portfolio data;
- retrieve market data directly from external providers.

---

# Architecture

```
DashboardController
        │
        ▼
DashboardService
        │
        ▼
 ┌───────────────────────────────┐
 │ Clients                       │
 │                               │
 │ • PortfolioClient             │
 │ • MarketClient                │
 │ • CreditClient                │
 └───────────────────────────────┘
        │
        ▼
 Snapshot Mappers
        │
        ▼
 Calculators
        │
        ▼
 DashboardDto
```

---

# Business Flow

```
Get Portfolio Positions
        │
        ▼
Retrieve Current Prices
        │
        ▼
Build Portfolio Snapshot
        │
        ▼
Retrieve Credit
        │
        ▼
Build Credit Snapshot
        │
        ▼
Calculate

• Portfolio Value
• Invested Amount
• Profit / Loss
• Remaining Credit
• Net Worth

        │
        ▼
Build DashboardDto
```

---

# Project Structure

```
controller/

service/

client/

calculator/

mapper/

model/

dto/

config/

exception/
```

---

# Dashboard Metrics

The Analytics Service currently calculates:

- Total Portfolio Value
- Total Invested Amount
- Unrealized Profit / Loss
- Remaining Credit
- Net Worth

---

# Snapshot Models

Snapshot models isolate business calculations from external service contracts.

Current snapshot models:

- PortfolioSnapshotPosition
- CreditSnapshot

---

# Calculators

Each calculator has a single responsibility.

Current calculators:

- PortfolioValueCalculator
- InvestedAmountCalculator
- ProfitLossCalculator
- RemainingCreditCalculator
- NetWorthCalculator

DashboardService orchestrates all calculations but does not contain calculation logic itself.

---

# Dependencies

Analytics Service communicates with:

- Portfolio Service
- Market Data Service
- Credit Service

Authentication is forwarded using the Authorization header received from the API Gateway.

---

# REST API

## Get Dashboard

```
GET /dashboard
```

Header:

```
Authorization: Bearer <JWT_TOKEN>
```

Example response:

```json
{
  "totalPortfolioValue": {
    "amount": 25400.15,
    "currency": "USD"
  },
  "totalInvestedAmount": {
    "amount": 23120.00,
    "currency": "USD"
  },
  "unrealizedProfitLoss": {
    "amount": 2280.15,
    "currency": "USD"
  },
  "remainingCredit": {
    "amount": 8000.00,
    "currency": "USD"
  },
  "netWorth": {
    "amount": 17400.15,
    "currency": "USD"
  },
  "calculatedAt": "2026-08-06T14:30:22Z"
}
```

---

# Swagger

```
http://localhost:8085/swagger-ui/index.html
```

---

# Health Endpoint

```
GET /actuator/health
```

---

# Running the Service

## Prerequisites

- Java 23
- Maven
- PostgreSQL
- Portfolio Service
- Market Data Service
- Credit Service

---

## Build

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

or run the application directly from IntelliJ IDEA.

---

# Testing

Run all tests:

```bash
mvn clean test
```

---

# Error Handling

The service uses centralized exception handling.

Response format:

```json
{
  "timestamp": "...",
  "status": 500,
  "code": "INTERNAL_SERVER_ERROR",
  "message": "..."
}
```

---

# MVP Limitations

Current MVP assumptions:

- Single dashboard currency.
- No currency conversion.
- Dashboard is read-only.
- Portfolio calculations are position-based.
- Transaction-based portfolio management will be introduced in the next development phase.

---

# Future Improvements

- Currency conversion before dashboard calculations.
- User-selected dashboard currency.
- Historical portfolio analytics.
- Realized profit/loss analytics based on transaction data provided by Portfolio Service.
- Dividend analytics.
- Batch market price retrieval.
- Market data caching.
- Portfolio performance and allocation metrics.