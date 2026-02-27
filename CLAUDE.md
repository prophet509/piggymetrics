# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

### Maven Build
```bash
# Build entire project (run tests)
mvn clean install

# Build without tests
mvn clean package -DskipTests

# Build specific service module
cd <service-name> && mvn clean package

# Run specific service (requires config service running first)
cd <service-name> && mvn spring-boot:run
```

### Docker Development
```bash
# Production mode - pulls pre-built images from Docker Hub
docker-compose up

# Development mode - builds images locally, exposes ports for debugging
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

# Build all services locally
docker-compose -f docker-compose.yml -f docker-compose.dev.yml build

# Rebuild specific service
docker-compose -f docker-compose.yml -f docker-compose.dev.yml build <service-name>
```

### Testing
```bash
# Run all tests
mvn test

# Run tests for specific service
cd <service-name> && mvn test

# Run a single test class
mvn test -Dtest=AccountServiceApplicationTests

# Run with coverage (Travis CI uses codecov)
mvn test jacoco:report
```

## Architecture Overview

PiggyMetrics is a microservices application demonstrating Spring Cloud patterns. The architecture consists of:

### Infrastructure Services (startup order matters)
1. **config** (port 8888) - Spring Cloud Config server with native profile
2. **registry** (port 8761) - Eureka service discovery
3. **gateway** (port 4000, exposed as 80) - API Gateway using Spring Cloud Gateway
4. **rabbitmq** (ports 5672, 15672) - Message broker for Spring Cloud Bus

### Business Services
Each business service has its own MongoDB instance for data isolation:
- **auth-service** (port 5000) - OAuth2 authorization server, user management
- **account-service** (port 6000) - Account data, income/expenses, savings
- **statistics-service** (port 7000) - Time-series data, cash flow analytics
- **notification-service** (port 8000) - Email notifications, settings

### Monitoring Services
- **monitoring** (port 8080, exposed as 9000) - Hystrix Dashboard, Turbine
- **turbine-stream-service** (port 8989) - Aggregates Hystrix metrics via RabbitMQ

### Key Development Endpoints
- Gateway: http://localhost:80
- Eureka Dashboard: http://localhost:8761
- Hystrix Dashboard: http://localhost:9000/hystrix (use stream: `http://turbine-stream-service:8989/turbine/turbine.stream`)
- RabbitMQ Management: http://localhost:15672 (guest/guest)

## Configuration Management

### Config Server Structure
All configuration is centralized in `config/src/main/resources/shared/`:
- `application.yml` - Shared across all services
- `<service-name>.yml` - Service-specific configuration

### Bootstrap Configuration
Each service uses `bootstrap.yml` (not `application.yml`) to connect to config:
```yaml
spring:
  application:
    name: account-service
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
```

Services authenticate with config service using `CONFIG_SERVICE_PASSWORD` from `.env`.

### Dynamic Configuration Refresh
Services using `@RefreshScope` can refresh config without restart:
```bash
curl -H "Authorization: Bearer #token#" -XPOST http://127.0.0.1:8000/notifications/refresh
```

## Inter-Service Communication

### Feign Clients with Circuit Breakers
Services communicate via OpenFeign with Hystrix circuit breakers:
```java
@FeignClient(name = "statistics-service", fallback = StatisticsServiceClientFallback.class)
public interface StatisticsServiceClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/statistics/{accountName}")
    void updateStatistics(@PathVariable("accountName") String accountName, Account account);
}
```

Service discovery via Eureka - no hardcoded addresses needed.

### OAuth2 Authentication
- **User authorization**: Password credentials grant (UI only)
- **Service-to-service**: Client credentials grant with `server` scope
- Each service has its own OAuth2 client credentials (e.g., `ACCOUNT_SERVICE_PASSWORD`)

Protected endpoints use:
```java
@PreAuthorize("#oauth2.hasScope('server')")
```

## MongoDB Data Isolation

Each service has its own MongoDB database with unique ports:
- auth-mongodb: port 25000 (dev mode)
- account-mongodb: port 26000 (dev mode)
- statistics-mongodb: port 27000 (dev mode)
- notification-mongodb: port 28000 (dev mode)

All databases named `piggymetrics` but logically isolated.

Account service includes a demo data dump (`account-service-dump.js`) for testing.

## Running Services Locally (IDE)

To run services in IntelliJ IDEA or other IDEs:

1. Export environment variables from `.env`:
   ```bash
   export CONFIG_SERVICE_PASSWORD=password
   export NOTIFICATION_SERVICE_PASSWORD=password
   export STATISTICS_SERVICE_PASSWORD=password
   export ACCOUNT_SERVICE_PASSWORD=password
   export MONGODB_PASSWORD=password
   ```

2. Start infrastructure first:
   - Config service
   - Registry service

3. Start MongoDB instances (via docker-compose or individual containers)

4. Start business services in dependency order

## Docker Network Service Names

When running in Docker, services use these DNS names:
- `config` - Config service
- `registry` - Eureka server
- `auth-service` - Auth service
- `account-service` - Account service
- `statistics-service` - Statistics service
- `notification-service` - Notification service
- `gateway` - API Gateway
- `<service>-mongodb` - MongoDB instances

Update URLs accordingly when switching between local and Docker execution.

## Distributed Tracing

Spring Cloud Sleuth adds trace/span IDs to logs:
```
[appname,traceId,spanId,exportable]
```

This helps track requests across service boundaries in logs.