# Analytics Service



## Overview



Analytics Service is responsible for aggregating investment data from multiple microservices and calculating portfolio-level analytical metrics for the Investment Tracker platform.



The service does not store investment data. Instead, it collects current portfolio state, transaction analytics, market prices and credit information from other services, performs business calculations, and returns a unified dashboard response.



---



## Responsibilities



The Analytics Service is responsible for:



- Aggregating current portfolio data.

- Retrieving transaction analytics from Portfolio Service.

- Retrieving current market prices.

- Retrieving current credit information.

- Calculating portfolio-level analytical metrics.

- Aggregating realized profit/loss.

- Calculating unrealized and total profit/loss.

- Returning a unified dashboard response.



The service does **not**:



- manage users;

- execute buy/sell operations;

- own or store transactions;

- calculate transaction-level realized profit/loss;

- store portfolio data;

- retrieve market data directly from external providers.



Transaction ownership and transaction-level calculations belong to Portfolio Service.



---



# Architecture



```text

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

│   ├─ Positions                │

│   └─ Transaction Summary      │

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



### Service Responsibility Boundaries



```text

Portfolio Service

        │

        ├─ Owns Positions

        ├─ Owns Transactions

        ├─ Processes BUY / SELL operations

        └─ Calculates realized P/L

           at transaction level

                │

                ▼

        Transaction Summary

                │

                ▼

Analytics Service

        │

        ├─ Aggregates realized P/L

        ├─ Calculates unrealized P/L

        ├─ Calculates total P/L

        ├─ Calculates portfolio metrics

        └─ Builds DashboardDto

```



Analytics Service does not reconstruct realized profit/loss from transaction data. Transaction-level realized P/L is calculated by Portfolio Service and exposed to Analytics Service through transaction analytics.



---



# Business Flow



```text

Get Portfolio Positions

        │

        ▼

Retrieve Current Prices

        │

        ▼

Build Portfolio Snapshot

        │

        ├─────────────────────────────┐

        │                             │

        ▼                             ▼

Calculate                    Get Transaction Summary

                              from Portfolio Service

• Portfolio Value                     │

• Invested Amount                     ▼

• Unrealized P/L              Realized P/L

        │                             │

        └──────────────┬──────────────┘

                       │

                       ▼

                Calculate Total P/L

                       │

                       ▼

                Retrieve Credit

                       │

                       ▼

                Build Credit Snapshot

                       │

                       ▼

                Calculate

 

                • Remaining Credit

                • Net Worth

 

                       │

                       ▼

                Build DashboardDto

```



---



# Project Structure



```text

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



The Analytics Service currently calculates or aggregates:



### Total Portfolio Value



Current market value of all open portfolio positions.



### Total Invested Amount



Total cost basis of the currently held portfolio positions.



### Unrealized Profit / Loss



Profit or loss associated with positions that are still held.



It is calculated from the current portfolio state and current market prices.



```text

Unrealized P/L =

(Current Price - Average Price) × Quantity

```



### Realized Profit / Loss



Profit or loss that has already been realized through SELL transactions.



Portfolio Service calculates realized P/L at transaction level. Analytics Service retrieves the aggregated transaction result and exposes it as part of portfolio analytics.



BUY transactions do not contribute realized P/L.



### Total Profit / Loss



Combined result of realized and unrealized investment performance.



```text

Total P/L = Unrealized P/L + Realized P/L

```



### Remaining Credit



Remaining credit balance retrieved from credit data and processed by Analytics Service.



### Net Worth



Current portfolio value after accounting for the remaining credit balance.



```text

Net Worth = Total Portfolio Value - Remaining Credit

```



---



# Snapshot Models



Snapshot models isolate business calculations from external service contracts.



Current snapshot models:



- PortfolioSnapshotPosition

- CreditSnapshot



Portfolio snapshots represent the current position state enriched with current market prices. Transaction-level realized profit/loss is not recalculated through portfolio snapshots.



---



# Calculators



Each calculator has a single responsibility.



Current calculators:



- PortfolioValueCalculator

- InvestedAmountCalculator

- ProfitLossCalculator

- TotalProfitLossCalculator

- RemainingCreditCalculator

- NetWorthCalculator



`DashboardService` orchestrates all calculations but does not contain calculation logic itself.



---



# Dependencies



Analytics Service communicates with:



### Portfolio Service



Provides:



- Current portfolio positions.

- Transaction analytics / transaction summary.

- Aggregated realized profit/loss based on stored transactions.



Portfolio Service owns transaction data and calculates realized P/L for individual SELL transactions.



Analytics Service does not duplicate this transaction-level calculation.



### Market Data Service



Provides current market prices required for position-based portfolio calculations.



### Credit Service



Provides current credit information required for remaining credit and net-worth calculations.



Authentication is forwarded using the `Authorization` header received from the API Gateway.



The same authorization context is forwarded when retrieving both portfolio position data and transaction analytics.



---



# REST API



## Get Dashboard



```text

GET /dashboard

```



Header:



```text

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

  "realizedProfitLoss": {

    "amount": 620.00,

    "currency": "USD"

  },

  "totalProfitLoss": {

    "amount": 2900.15,

    "currency": "USD"

  },

  "calculatedAt": "2026-08-13T09:30:22Z"

}

```



No separate public Analytics Service endpoint is required for realized or total profit/loss. These metrics are exposed through the existing dashboard endpoint.



---



# Swagger



```text

http://localhost:8085/swagger-ui/index.html

```



Swagger/OpenAPI exposes the current `DashboardDto`, including realized and total profit/loss metrics.



---



# Health Endpoint



```text

GET /actuator/health

```



---



# Running the Service



## Prerequisites



- Java 23

- Maven

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



The test suite covers dashboard orchestration, calculators, snapshot mapping, transaction analytics integration, empty portfolio scenarios and dashboards without active credit.



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

- Current portfolio metrics are based on the current Position state.

- Historical portfolio state is not yet available.

- Transaction ownership and transaction-level realized P/L calculation remain the responsibility of Portfolio Service.



---



# Future Improvements



Future improvements within Analytics Service responsibilities:



- Currency conversion before dashboard calculations.

- User-selected dashboard currency.

- Historical portfolio analytics.

- Dividend analytics.

- Batch market price retrieval.

- Market data caching.

- Portfolio performance metrics.

- Portfolio allocation analytics.