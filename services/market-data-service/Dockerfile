FROM eclipse-temurin:23.0.2_7-jre

WORKDIR /app

COPY target/market-data-service-0.0.1-SNAPSHOT.jar market-data-service.jar

ENTRYPOINT ["java", "-jar", "market-data-service.jar"]