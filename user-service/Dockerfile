# Multi-stage build
# Faza 1 - Build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiraj parent pom
COPY pom.xml .

# Kopiraj user-service
COPY user-service/pom.xml user-service/
COPY user-service/src user-service/src

# Build
RUN mvn -pl user-service -am clean package -DskipTests

# Faza 2 - Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/user-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]