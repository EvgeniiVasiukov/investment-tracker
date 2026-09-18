FROM eclipse-temurin:23.0.2_7-jre

WORKDIR /app

COPY target/credit-service-0.0.1-SNAPSHOT.jar credit-service.jar

ENTRYPOINT ["java", "-jar", "credit-service.jar"]