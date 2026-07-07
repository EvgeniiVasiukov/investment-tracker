# Local Development Guide

## Purpose
This document describes how to set up and run the Investment Tracker project locally.

It contains instructions for:
- required software;
- project structure;
- running individual microservices;
- starting services from IntelliJ IDEA and terminal;
- troubleshooting common local development issues.

## Prerequisites
Before running the project, make sure the following software is installed:

- Git
- Java 25
- IntelliJ IDEA Ultimate (recommended)
- Maven Wrapper (included in every microservice)
- PostgreSQL

## Repository Structure
The project is organized as a monorepository.

investment-tracker/
├── docs/
├── infra/
├── services/
│   ├── market-data-service/
│   ├── portfolio-service/
│   └── user-service/
└── ...

Each microservice is an independent Maven project with its own pom.xml.

Microservices can be built and started independently.
## Running Services

Each microservice can be built and started independently.

Navigate to the corresponding service directory before executing any Maven commands.


### User Service

### Portfolio Service

### Market Data Service
#### Build

**macOS / Linux**
```bash

cd services/market-data-service
./mvnw clean package
```

**Windows**

```cmd

cd services\market-data-service
mvnw.cmd clean package
```

#### Run

**macOS / Linux**

```bash

./mvnw spring-boot:run
```

**Windows**

```cmd

mvnw.cmd spring-boot:run
```
## Running from IntelliJ IDEA

Currently, each microservice should be opened as an individual IntelliJ IDEA project.

Until a root Maven aggregator project is introduced, opening the entire monorepository does not allow IntelliJ IDEA to correctly recognize all Maven modules.

Open one of the following directories:

- services/market-data-service
- services/portfolio-service
- services/user-service

After opening the project:

1. Wait until Maven finishes importing dependencies.
2. Open the *Application.java class.
3. Start the application using the green Run button.

## Running from Terminal

## Troubleshooting

### Port is already in use

If the application fails to start because the port is already in use:

macOS / Linux

```bash
lsof -i :<PORT>
kill -9 <PID>
```
Windows

```bash
netstat -ano | findstr :<PORT>
taskkill /PID <PID> /F
```

---

### Maven Wrapper

macOS / Linux

Always use:

./mvnw

Windows

Always use:

mvnw.cmd

## Useful Commands

### Build

**macOS / Linux**
```
./mvnw clean package
```

**Windows**
```
mvnw.cmd clean package
```

### Run tests

**macOS / Linux**
```
./mvnw test
```

**Windows**
```
mvnw.cmd test
```
### Clean

**macOS / Linux**
```
./mvnw clean
```

**Windows**

```
mvnw.cmd clean
```

### Run application

**macOS / Linux**
```
./mvnw spring-boot:run
```
**Windows**
```
mvnw.cmd spring-boot:run
```