# Stage 1: Build environment (optional)
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -DskipTests


# Stage 2: Slim image with application
FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 8080

## Add a non-root user
#RUN addgroup -S demo && adduser -S demo -G demo
#USER demo

ENTRYPOINT ["java", "-jar", "app.jar"]