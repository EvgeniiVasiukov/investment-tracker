# Analytics Service v0.7.0 — Advanced Portfolio Analytics

## Overview

Version 0.7.0 extends the Analytics Service with transaction-based
portfolio analytics and additional profit/loss metrics.

## Added

- Realized profit/loss integration based on transaction analytics
  provided by Portfolio Service.
- Total portfolio profit/loss calculation.
- Transaction Summary integration with Portfolio Service.
- Extended Dashboard response with:
    - Realized P/L
    - Total P/L

## Changed

- Dashboard orchestration now combines current position-based analytics
  with transaction-based analytics.
- Dashboard documentation has been updated to reflect the new metrics.
- Authorization is forwarded when retrieving transaction analytics.

## Analytics Model

The service now distinguishes between:

- Unrealized P/L — calculated from currently held positions and
  current market prices.
- Realized P/L — calculated at transaction level by Portfolio Service
  and aggregated for analytics.
- Total P/L — calculated as:

  Total P/L = Unrealized P/L + Realized P/L

## Service Boundaries

Portfolio Service
- Owns transactions.
- Processes BUY and SELL operations.
- Calculates realized P/L at transaction level.

Analytics Service
- Aggregates realized P/L.
- Calculates unrealized P/L.
- Calculates total P/L.
- Builds portfolio-level dashboard analytics.

## API

The existing endpoint remains unchanged:

GET /dashboard

No additional public endpoint was introduced for the new analytics.

## Testing

- Dashboard orchestration tests updated.
- Realized and total P/L scenarios covered.
- Empty transaction history covered.
- Existing empty portfolio and missing credit scenarios remain supported.
- Full test suite passes with mvn clean test.