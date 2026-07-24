# API Gateway

API Gateway is the single entry point for all Investment Tracker microservices.

## Features

- Spring Cloud Gateway
- JWT authentication
- Public and protected endpoints
- Request routing to downstream services

---

## Prerequisites

- Java 23
- Maven
- Environment variable:

USER_SERVICE_SECRET_KEY=<your-secret-key>

The secret must be identical to the one used by user-service.

---

## Configuration

Example:

server:
port: 8086

jwt:
secret: ${USER_SERVICE_SECRET_KEY}

---

## Running the application

mvn spring-boot:run

or run ApiGatewayApplication from your IDE.

---

## Authentication

The gateway validates JWT tokens before forwarding requests to downstream services.

### Public endpoints

| Method | Endpoint |
|---------|----------|
| POST | /auth/register |
| POST | /auth/login |
| GET | /actuator/health |

These endpoints do not require authentication.

---

### Protected endpoints

All other endpoints require a valid JWT token.

Example request:

GET /users
Authorization: Bearer <your-jwt-token>

---

## Getting a JWT token

Authenticate using:

POST /auth/login

Example request:

{
"email": "user@example.com",
"password": "password"
}

The response contains a JWT token.

Use the token in every protected request:

Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

---

## Authentication flow

1. Client sends credentials to /auth/login.
2. user-service authenticates the user.
3. user-service generates a JWT.
4. Client stores the JWT.
5. Client sends the JWT with every protected request.
6. API Gateway validates the JWT.
7. If valid, the request is forwarded.
8. If invalid or expired, the gateway returns 401 Unauthorized.

---

## Response codes

| Status | Description |
|---------|-------------|
| 200 | Request processed successfully |
| 401 | Missing, invalid or expired JWT |

## Error Handling

The API Gateway returns standardized JSON responses for all gateway-level errors.

### Response format

JSON

{
"timestamp": "2026-07-23T16:07:00.776984Z",
"status": 404,
"errorCode": "ROUTE_NOT_FOUND",
"message": "Route not found"
}
### Supported gateway error codes

| HTTP Status | Error Code | Description |
|-------------|------------|-------------|
| 401 | UNAUTHORIZED | Missing or invalid JWT token. |
| 403 | FORBIDDEN | Access to the requested resource is denied. |
| 404 | ROUTE_NOT_FOUND | The requested route does not exist in the API Gateway. |
| 500 | INTERNAL_GATEWAY_ERROR | Unexpected internal error in the API Gateway. |

### Notes

- Only gateway-level errors are handled by the API Gateway.
- Business errors returned by downstream microservices are passed through without modification.
- All gateway error responses include:
    - timestamp
    - status
    - errorCode
    - message