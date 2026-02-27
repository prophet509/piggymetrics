set shell := ["bash", "-c"]

# Default recipe
default: help

# Show available commands
help:
    @just --list

# Build all microservices using Maven
build:
    @echo "Building all microservices with Maven..."
    ./mvnw clean package -DskipTests

# Build Docker images for all microservices
build-images:
    @echo "Building Docker images for all microservices..."
    DOCKER_API_VERSION="1.44" ./mvnw spring-boot:build-image -DskipTests

# Start all services using Docker Compose
start:
    @echo "Starting all services..."
    docker compose up -d

# View logs for all services (press Ctrl+C to exit)
logs:
    docker compose logs -f

# Stop all services
stop:
    @echo "Stopping all services..."
    docker compose down

# Restart all services (stop and then start)
restart: stop start

# Remove all containers, networks, and volumes
clean:
    @echo "Cleaning up Docker resources..."
    docker compose down -v
    ./mvnw clean

# Build everything and start
up: build-images start
