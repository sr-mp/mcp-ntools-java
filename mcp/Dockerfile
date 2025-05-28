# syntax=docker/dockerfile:1

# Use an official OpenJDK image as a parent image
FROM eclipse-temurin:21-jdk-jammy

# Install nmap
RUN apt-get update && apt-get install -y nmap && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the built jar from the host (outside the container)
COPY target/mcp-1.0.0.jar ./mcp.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the MCP server
ENTRYPOINT ["java", "-jar", "mcp.jar"]
