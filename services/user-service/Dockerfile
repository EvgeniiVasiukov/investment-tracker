FROM eclipse-temurin:23.0.2_7-jre

WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]