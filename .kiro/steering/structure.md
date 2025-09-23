# Project Structure & Organization

## Root Project Structure
```
muying-mall-microservices/
├── muying-mall-common/          # Shared utilities and common components
├── muying-mall-gateway/         # API Gateway service (8080)
├── muying-mall-user/            # User management service (8001)
├── muying-mall-product/         # Product catalog service (8002)
├── muying-mall-order/           # Order processing service (8003)
├── muying-mall-payment/         # Payment processing service (8004)
├── muying-mall-search/          # Search and discovery service (8005)
├── muying-mall-logistics/       # Logistics management service (8006)
├── muying-mall-comment/         # Reviews and ratings service (8007)
├── muying-mall-points/          # Loyalty points service (8008)
├── muying-mall-admin/           # Admin dashboard service (8009)
├── docker/                      # Docker configurations
├── docs/                        # Project documentation
├── config/                      # External configuration files
├── sql/                         # Database scripts
└── logs/                        # Application logs
```

## Individual Service Structure
Each microservice follows this standard Maven structure:
```
muying-mall-{service}/
├── src/main/java/com/muyingmall/{service}/
│   ├── controller/              # REST API controllers
│   ├── service/                 # Business logic services
│   │   └── impl/               # Service implementations
│   ├── mapper/                  # MyBatis mappers
│   ├── entity/                  # Database entities
│   ├── dto/                     # Data transfer objects
│   ├── vo/                      # View objects
│   ├── config/                  # Configuration classes
│   ├── exception/               # Custom exceptions
│   └── {ServiceName}Application.java  # Main application class
├── src/main/resources/
│   ├── mapper/                  # MyBatis XML mappers
│   ├── application.yml          # Application configuration
│   └── bootstrap.yml            # Bootstrap configuration (Nacos)
├── src/test/                    # Test classes
├── Dockerfile                   # Docker build file
└── pom.xml                      # Maven configuration
```

## Common Module Organization
```
muying-mall-common/src/main/java/com/muyingmall/common/
├── result/                      # Unified response classes
│   ├── Result.java             # Standard API response wrapper
│   └── PageResult.java         # Paginated response wrapper
├── exception/                   # Global exception handling
│   ├── GlobalExceptionHandler.java
│   └── BusinessException.java
├── utils/                       # Utility classes
│   ├── JwtUtils.java           # JWT token utilities
│   ├── RedisUtil.java          # Redis operations
│   ├── CacheProtectionUtil.java # Cache protection
│   └── PasswordEncoder.java    # Password encryption
├── enums/                       # System enumerations
│   ├── OrderStatus.java
│   ├── PaymentStatus.java
│   └── UserStatus.java
├── config/                      # Common configurations
│   ├── RedisConfig.java
│   ├── WebConfig.java
│   └── FeignConfig.java
└── constants/                   # System constants
    └── CommonConstants.java
```

## Naming Conventions

### Package Naming
- Base package: `com.muyingmall.{service}`
- Controllers: `com.muyingmall.{service}.controller`
- Services: `com.muyingmall.{service}.service`
- Entities: `com.muyingmall.{service}.entity`
- DTOs: `com.muyingmall.{service}.dto`

### Class Naming
- **Controllers**: `{Entity}Controller` (e.g., `UserController`)
- **Services**: `{Entity}Service` interface, `{Entity}ServiceImpl` implementation
- **Entities**: `{Entity}` (e.g., `User`, `Product`)
- **DTOs**: `{Entity}DTO` (e.g., `UserDTO`)
- **VOs**: `{Entity}VO` (e.g., `UserVO`)
- **Mappers**: `{Entity}Mapper` (e.g., `UserMapper`)

### Method Naming
- **CRUD Operations**: `save`, `update`, `delete`, `findById`, `findAll`
- **Query Methods**: `findBy{Condition}`, `listBy{Condition}`
- **Business Methods**: Use descriptive verbs (e.g., `processOrder`, `calculatePoints`)

## Configuration Management

### Application Configuration Files
- **bootstrap.yml**: Nacos connection and service discovery
- **application.yml**: Local development configuration
- **application-docker.yml**: Docker environment configuration
- **application-prod.yml**: Production environment configuration

### Nacos Configuration Structure
```
Data ID Pattern: {spring.application.name}-{profile}.{file-extension}
Group: DEFAULT_GROUP (or service-specific groups)
Examples:
- muying-mall-user-dev.yml
- muying-mall-product-prod.yml
- muying-mall-gateway-docker.yml
```

## Database Organization

### Database Per Service
Each microservice maintains its own database:
- `user_db`: User service database
- `product_db`: Product service database  
- `order_db`: Order service database
- `payment_db`: Payment service database
- And so on...

### Table Naming
- Use snake_case: `user_addresses`, `product_categories`
- Prefix with service context when needed: `order_items`, `payment_records`
- Include audit fields: `created_at`, `updated_at`, `created_by`, `updated_by`

## API Design Patterns

### REST Endpoint Structure
```
/{service-context}/{resource}/{id?}/{sub-resource?}
Examples:
- GET /users/{id}
- POST /users
- GET /users/{id}/addresses
- POST /orders/{id}/payments
```

### Response Format
All APIs return standardized `Result<T>` wrapper:
```json
{
  "code": 200,
  "message": "Success",
  "data": {...},
  "timestamp": "2025-09-18T10:30:00"
}
```

## Docker & Deployment Structure

### Dockerfile Location
Each service has its own `Dockerfile` in the service root directory.

### Docker Compose Services
- **Infrastructure**: mysql, redis, nacos, sentinel-dashboard, zipkin, elasticsearch, kibana
- **Application Services**: gateway, user-service, product-service, etc.
- **Networks**: All services use `muying-mall-network`
- **Volumes**: Persistent storage for databases and logs

## Documentation Structure
```
docs/
├── ARCHITECTURE.md              # System architecture overview
├── microservices-*.md          # Service-specific documentation
├── nacos-config-import-guide.md # Configuration management
├── RabbitMQ*.md                # Message queue documentation
└── images/                     # Architecture diagrams
```

## Development Workflow

### Branch Strategy
- `main`: Production-ready code
- `develop`: Integration branch
- `feature/*`: Feature development branches
- `hotfix/*`: Production hotfixes

### Code Organization Principles
1. **Separation of Concerns**: Clear separation between layers
2. **Dependency Injection**: Use Spring's IoC container
3. **Configuration Externalization**: Environment-specific configs in Nacos
4. **Error Handling**: Centralized exception handling
5. **Logging**: Structured logging with correlation IDs
6. **Testing**: Unit tests for services, integration tests for APIs