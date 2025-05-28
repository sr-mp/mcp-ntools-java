# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk-jammy
RUN apt-get update && apt-get install -y nmap && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /build/target/mcp-1.0.0.jar ./mcp.jar
COPY --from=build /build/target/test-classes ./test-classes
COPY --from=build /build/target/classes ./classes
COPY --from=build /build/pom.xml ./pom.xml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mcp.jar"]
