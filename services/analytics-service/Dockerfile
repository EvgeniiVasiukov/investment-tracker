FROM eclipse-temurin:23.0.2_7-jre

WORKDIR /app

COPY target/analytics-service-0.0.1-SNAPSHOT.jar analytics-service.jar

ENTRYPOINT ["java", "-jar", "analytics-service.jar"]