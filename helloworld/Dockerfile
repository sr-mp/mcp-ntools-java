# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jre

# Set the working directory
WORKDIR /app

# Copy the built jar from the target directory (relative to build context)
COPY target/helloworld-1.0.0.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
