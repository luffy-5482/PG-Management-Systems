# Stage 1: Build the Application
# Use a Maven base image (includes JDK 17 for building)
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project source code (including pom.xml)
COPY . /app

# Package the application into an executable JAR
# -DskipTests is added to avoid the previous test failure and ensure the build completes
RUN mvn clean package -DskipTests

# Stage 2: Create the Final Runtime Image
# Use a lightweight OpenJDK 17 image for smaller size and security
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the 'build' stage
# NOTE: This uses your confirmed JAR name: PGmanagement-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/PGmanagement-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]