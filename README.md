# Spring Boot Application with Docker and SQL Server

This project demonstrates containerization of a Spring Boot application using Docker and Docker Compose with SQL Server.

## Project Structure

- `Dockerfile` - Defines the Spring Boot application container
- `docker-compose.yml` - Orchestrates both Spring Boot and SQL Server containers
- `.dockerignore` - Excludes unnecessary files from Docker build context
- `.env.example` - Example environment variables file
- `sql-scripts/init.sql` - SQL Server database initialization script
- `src/main/resources/application.properties` - Development configuration with SQL Server
- `src/main/resources/application-prod.properties` - Production configuration with SQL Server

## Prerequisites

- Docker Desktop installed
- Docker Compose installed

## Running the Application

### Using Docker Compose (Recommended)

1. Build and start the containers:
   ```bash
   docker-compose up --build
   ```

2. The application will be available at:
   - Spring Boot App: http://localhost:8080
   - SQL Server: Port 1433

3. To stop the containers:
   ```bash
   docker-compose down
   ```

### Environment Variables

The following environment variables can be customized in `.env.example`:

- `SPRING_PROFILES_ACTIVE` - Application profile (default: prod)
- `SERVER_PORT` - Server port (default: 8080)
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username (default: sa)
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - Hibernate DDL auto configuration

## Database Configuration

The application supports both:
1. **Development**: SQL Server database in Docker container (default)
2. **Production**: SQL Server database in Docker container

When running with Docker Compose, the application automatically uses the SQL Server configuration via the `application-prod.properties` profile.

## Notes

- The SQL Server container data is persisted using Docker volumes
- The Spring Boot application will automatically create the database schema
- Make sure port 8080 and 1433 are available on your host machine
