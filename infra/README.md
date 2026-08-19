# Local Infrastructure

This directory contains the Docker Compose configuration for the local Investment Tracker infrastructure.

## PostgreSQL Databases

The local infrastructure provides separate PostgreSQL instances for services that own persistent database storage.

| Service | Database | Host Port | Container Port |
|---|---|---:|---:|
| User Service | user_service_db | 5435 | 5432 |
| Portfolio Service | portfolio_db | 5433 | 5432 |
| Credit Service | credit_service_db | 5434 | 5432 |

Database data is stored in named Docker volumes and persists when the containers are stopped or recreated.

## Start Local Infrastructure

From the infra directory, run:

Bash

docker compose up -d

This starts all PostgreSQL containers in the background.

## Check Container Status

Bash

docker compose ps

All three PostgreSQL containers should be running.

## Stop Local Infrastructure

Bash

docker compose down

This stops and removes the containers created by Docker Compose.

Named database volumes are preserved.

## Configuration

Spring Boot services running locally connect to these databases using their local profile configuration.

The standardized local database ports are:

Plain text

Portfolio Service → localhost:5433
Credit Service    → localhost:5434
User Service      → localhost:5435

Service-specific Docker Compose files are no longer used. Local database infrastructure is managed centrally through:

Plain text

infra/docker-compose.yml
