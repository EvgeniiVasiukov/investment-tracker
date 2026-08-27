FROM eclipse-temurin:23.0.2_7-jre

WORKDIR /app

COPY target/portfolio-service-0.0.1-SNAPSHOT.jar portfolio-service.jar

ENTRYPOINT ["java", "-jar", "portfolio-service.jar"]