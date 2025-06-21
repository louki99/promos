# Partner Service - Professional Microservice Architecture

## Overview

The Partner Service is a comprehensive microservice designed to manage business partners, customers, and B2B relationships in the FoodPlus ordering system. It provides a complete solution for partner lifecycle management, loyalty programs, credit management, contract management, and business analytics.

## 🏗️ Architecture

### Microservice Patterns Implemented

- **Domain-Driven Design (DDD)**: Clean separation of domain, application, and infrastructure layers
- **CQRS Pattern**: Separate read and write operations for optimal performance
- **Event-Driven Architecture**: Asynchronous event publishing for service integration
- **Repository Pattern**: Abstracted data access layer
- **Service Layer Pattern**: Business logic encapsulation
- **DTO Pattern**: Data transfer objects for API contracts
- **Exception Handling**: Centralized error management with custom exceptions
- **Validation**: Comprehensive input validation with Bean Validation
- **Caching**: Multi-level caching strategy for performance optimization
- **Async Processing**: Background task execution for heavy operations

### Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL with JPA/Hibernate
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Mapping**: MapStruct for object mapping
- **Validation**: Bean Validation (Jakarta)
- **Caching**: Spring Cache with Redis support
- **Testing**: JUnit 5, Mockito, TestContainers
- **Monitoring**: Micrometer, Actuator
- **Security**: Spring Security (configurable)

## 🚀 Features

### Core Partner Management
- ✅ Complete CRUD operations for partners
- ✅ Partner lifecycle management (active/inactive)
- ✅ Unique constraint validation (CT number, ICE)
- ✅ Soft delete functionality
- ✅ Audit trail and versioning

### B2B Partner Features
- ✅ Company information management
- ✅ Contract management with expiration tracking
- ✅ Credit limit and payment history
- ✅ Business activity classification
- ✅ Annual turnover tracking
- ✅ Employee count management

### Loyalty & Rewards
- ✅ Loyalty points system
- ✅ VIP status management
- ✅ Order history tracking
- ✅ Spending analytics
- ✅ Loyalty level calculation (0-5 tiers)

### Credit Management
- ✅ Credit limit management
- ✅ Outstanding balance tracking
- ✅ Credit rating system (A, B, C)
- ✅ Credit score calculation
- ✅ Payment term management
- ✅ Overdue payment detection

### Group Management
- ✅ Partner group creation and management
- ✅ Group membership operations
- ✅ Hierarchical group structures
- ✅ Group-based analytics

### Advanced Search & Filtering
- ✅ Full-text search across multiple fields
- ✅ Filtering by partner type, status, credit rating
- ✅ Pagination support
- ✅ Sorting capabilities
- ✅ Complex query support

### Analytics & Reporting
- ✅ Comprehensive partner statistics
- ✅ Top performers identification
- ✅ Revenue and growth metrics
- ✅ Geographic distribution analysis
- ✅ Business activity insights
- ✅ Contract expiration alerts

### Event-Driven Integration
- ✅ Partner lifecycle events
- ✅ Loyalty point updates
- ✅ Credit limit changes
- ✅ VIP status changes
- ✅ Group membership events

## 📁 Project Structure

```
partner-service/
├── src/main/java/ma/foodplus/ordering/system/partner/
│   ├── config/                          # Configuration classes
│   │   ├── OpenApiConfig.java          # Swagger/OpenAPI configuration
│   │   ├── CacheConfig.java            # Caching configuration
│   │   └── AsyncConfig.java            # Async processing configuration
│   ├── controller/                      # REST API controllers
│   │   ├── PartnerController.java      # Main partner operations
│   │   ├── PartnerGroupController.java # Group management
│   │   └── PartnerStatisticsController.java # Analytics & reporting
│   ├── domain/                         # Domain entities and value objects
│   │   ├── Partner.java               # Main partner entity
│   │   ├── PartnerGroup.java          # Group entity
│   │   ├── ContactInfo.java           # Embedded contact information
│   │   ├── CompanyInfo.java           # Embedded company information
│   │   ├── ContractInfo.java          # Embedded contract information
│   │   ├── CreditInfo.java            # Embedded credit information
│   │   ├── LoyaltyInfo.java           # Embedded loyalty information
│   │   ├── DeliveryPreference.java    # Embedded delivery preferences
│   │   └── AuditInfo.java             # Embedded audit information
│   ├── dto/                           # Data Transfer Objects
│   │   ├── PartnerDTO.java            # Partner DTO with validation
│   │   ├── PartnerStatisticsDTO.java  # Statistics DTO
│   │   └── ErrorResponse.java         # Standardized error response
│   ├── event/                         # Event-driven architecture
│   │   ├── PartnerEvent.java          # Event model
│   │   ├── PartnerEventPublisher.java # Event publisher interface
│   │   └── impl/PartnerEventPublisherImpl.java # Event implementation
│   ├── exception/                     # Exception handling
│   │   ├── ErrorCode.java             # Error code enumeration
│   │   ├── PartnerException.java      # Custom exception class
│   │   └── GlobalExceptionHandler.java # Global exception handler
│   ├── mapper/                        # Object mapping
│   │   └── PartnerMapper.java         # MapStruct mapper interface
│   ├── repository/                    # Data access layer
│   │   ├── PartnerRepository.java     # Partner repository
│   │   └── PartnerGroupRepository.java # Group repository
│   ├── service/                       # Business logic layer
│   │   ├── PartnerService.java        # Service interface
│   │   └── impl/PartnerServiceImpl.java # Service implementation
│   └── PartnerServiceApplication.java # Main application class
```

## 🔧 API Endpoints

### Partner Management
```
POST   /api/v1/partners                    # Create partner
GET    /api/v1/partners                    # Get all partners (paginated)
GET    /api/v1/partners/{id}               # Get partner by ID
PUT    /api/v1/partners/{id}               # Update partner
DELETE /api/v1/partners/{id}               # Delete partner (soft delete)
GET    /api/v1/partners/by-ct-num/{ctNum}  # Get partner by CT number
GET    /api/v1/partners/by-ice/{ice}       # Get partner by ICE
GET    /api/v1/partners/search             # Search partners
```

### Partner Operations
```
POST   /api/v1/partners/{id}/activate      # Activate partner
POST   /api/v1/partners/{id}/deactivate    # Deactivate partner
POST   /api/v1/partners/{id}/loyalty-points # Update loyalty points
GET    /api/v1/partners/{id}/loyalty-level # Get loyalty level
POST   /api/v1/partners/{id}/credit-limit  # Update credit limit
GET    /api/v1/partners/{id}/total-spent   # Get total spent
```

### Group Management
```
GET    /api/v1/partner-groups/{groupId}/partners           # Get partners in group
POST   /api/v1/partner-groups/{groupId}/partners/{partnerId} # Add partner to group
DELETE /api/v1/partner-groups/{groupId}/partners/{partnerId} # Remove partner from group
GET    /api/v1/partner-groups/{groupId}/partners/{partnerId}/check # Check membership
```

### Analytics & Statistics
```
GET    /api/v1/partner-statistics/overview                 # Overview statistics
GET    /api/v1/partner-statistics/top-spenders             # Top spending partners
GET    /api/v1/partner-statistics/distribution/type        # Distribution by type
GET    /api/v1/partner-statistics/average-order-value      # Average order values
GET    /api/v1/partner-statistics/expiring-contracts       # Expiring contracts
GET    /api/v1/partner-statistics/overdue-payments         # Overdue payments
GET    /api/v1/partner-statistics/by-credit-rating/{rating} # By credit rating
GET    /api/v1/partner-statistics/by-business-activity     # By business activity
GET    /api/v1/partner-statistics/by-annual-turnover       # By annual turnover
GET    /api/v1/partner-statistics/vip                      # VIP partners
GET    /api/v1/partner-statistics/active                   # Active partners
```

## 🛠️ Configuration

### Application Properties
```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/foodplus_partners
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

# Cache Configuration
spring:
  cache:
    type: redis
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms

# OpenAPI Configuration
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method

# Logging Configuration
logging:
  level:
    ma.foodplus.ordering.system.partner: DEBUG
    org.springframework.cache: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

# Server Configuration
server:
  port: 8080
  servlet:
    context-path: /partner-service
```

## 🧪 Testing

### Unit Tests
```bash
# Run unit tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

### Integration Tests
```bash
# Run integration tests
./mvnw verify

# Run with TestContainers
./mvnw test -Dspring.profiles.active=test
```

### API Testing
```bash
# Start the application
./mvnw spring-boot:run

# Access Swagger UI
http://localhost:8080/partner-service/swagger-ui.html

# Test endpoints with curl
curl -X GET "http://localhost:8080/partner-service/api/v1/partners" \
  -H "Content-Type: application/json"
```

## 📊 Monitoring & Observability

### Health Checks
```
GET /actuator/health          # Application health
GET /actuator/info           # Application information
GET /actuator/metrics        # Application metrics
GET /actuator/prometheus     # Prometheus metrics
```

### Key Metrics
- Partner creation/update rates
- API response times
- Cache hit/miss ratios
- Database connection pool usage
- Error rates by endpoint
- Business metrics (VIP partners, active partners, etc.)

## 🔒 Security

### Authentication & Authorization
- JWT-based authentication (configurable)
- Role-based access control (RBAC)
- API key authentication for service-to-service communication
- Rate limiting and throttling

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- XSS protection
- Sensitive data encryption
- Audit logging for all operations

## 🚀 Deployment

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/partner-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: partner-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: partner-service
  template:
    metadata:
      labels:
        app: partner-service
    spec:
      containers:
      - name: partner-service
        image: foodplus/partner-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
```

## 🔄 Integration

### Event Publishing
The service publishes events for:
- Partner lifecycle changes
- Loyalty point updates
- Credit limit modifications
- VIP status changes
- Group membership updates

### Service Dependencies
- **Order Service**: For order history and spending analytics
- **Notification Service**: For alerts and notifications
- **Payment Service**: For payment processing integration
- **Analytics Service**: For advanced reporting

## 📈 Performance Optimization

### Caching Strategy
- **L1 Cache**: Entity-level caching with Hibernate
- **L2 Cache**: Application-level caching with Redis
- **Query Cache**: Frequently accessed data caching
- **Statistics Cache**: Pre-computed analytics caching

### Database Optimization
- Indexed queries for common operations
- Connection pooling
- Query optimization
- Read replicas for analytics

### Async Processing
- Background task execution
- Event processing
- Report generation
- Data synchronization

## 🛡️ Error Handling

### Error Codes
- **5000-5099**: Partner-related errors
- **5100-5199**: Validation errors
- **5200-5299**: Business logic errors
- **9000-9999**: System errors

### Standardized Error Response
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input data",
  "path": "/api/v1/partners",
  "errorCode": 5100,
  "details": ["CT number must be alphanumeric"]
}
```

## 📚 Documentation

### API Documentation
- Complete OpenAPI 3.0 specification
- Interactive Swagger UI
- Request/response examples
- Error code documentation

### Code Documentation
- Comprehensive JavaDoc
- Architecture decision records (ADRs)
- Design patterns documentation
- Integration guides

## 🤝 Contributing

### Development Setup
1. Clone the repository
2. Install dependencies: `./mvnw clean install`
3. Set up database and Redis
4. Configure application properties
5. Run tests: `./mvnw test`
6. Start application: `./mvnw spring-boot:run`

### Code Standards
- Follow Spring Boot best practices
- Use consistent naming conventions
- Write comprehensive tests
- Document public APIs
- Follow SOLID principles

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation
- Review the troubleshooting guide

---

**Partner Service** - Professional microservice for comprehensive partner management in the FoodPlus ecosystem. 