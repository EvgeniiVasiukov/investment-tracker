# Architecture Decisions

This document describes the architectural decisions made during the development of Investment Tracker.

---

# ADR-001 — Monorepository

## Status

Accepted

## Decision

The project uses a monorepository.

investment-tracker
├── services
├── infra
└── docs
## Motivation

All microservices belong to one product.

The monorepository simplifies:

- development
- documentation
- Docker Compose
- dependency management
- project navigation

## Alternatives

Multiple repositories.

## Why rejected

It would complicate development without providing significant benefits for a project of this size.