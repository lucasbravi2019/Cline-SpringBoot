# Use Maven image for building the application
FROM maven:3.9.6-amazoncorretto-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Copy source files
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use Amazon Corretto 21 image for running the application (more reliable)
FROM amazoncorretto:21-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/spring-app-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the application using java -jar
ENTRYPOINT ["java", "-jar", "app.jar"]
