# Technology Stack & Build System

## Core Framework
- **Java**: 21 (Latest LTS)
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2025.0.0
- **Spring Cloud Alibaba**: 2023.0.1.2
- **Build Tool**: Maven 3.9.9+

## Microservices Stack
- **Service Discovery**: Nacos 3.0.2
- **Configuration Management**: Nacos Config
- **API Gateway**: Spring Cloud Gateway
- **Service Communication**: OpenFeign
- **Circuit Breaker**: Sentinel 1.8.x
- **Load Balancing**: Spring Cloud LoadBalancer
- **Distributed Tracing**: Micrometer + Zipkin

## Data Layer
- **Database**: MySQL 8.0.33
- **ORM**: MyBatis Plus 3.5.9
- **Cache**: Redis 7.x (Spring Data Redis)
- **Search Engine**: Elasticsearch 8.11.0
- **Connection Pooling**: HikariCP (default with Spring Boot)

## Security & Authentication
- **Authentication**: JWT 0.12.6 (jjwt)
- **Security Framework**: Spring Security
- **Password Encoding**: BCrypt

## Third-Party Integrations
- **Payment**: Alipay SDK 4.38.0.ALL, WeChat Pay SDK 0.0.3
- **Documentation**: SpringDoc OpenAPI 2.8.2
- **JSON Processing**: FastJSON2 2.0.55
- **Excel Processing**: Apache POI 5.3.0
- **Message Queue**: RabbitMQ (Spring AMQP 3.2.5)

## Development Tools
- **Containerization**: Docker & Docker Compose
- **Monitoring**: Micrometer 1.14.2
- **Logging**: SLF4J + Logback (Spring Boot default)
- **Testing**: Spring Boot Test, JUnit 5

## Common Build Commands

### Maven Commands
```bash
# Clean and compile all modules
mvn clean compile

# Package all services (skip tests for faster build)
mvn clean package -DskipTests

# Run specific service
cd muying-mall-{service-name}
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker Commands
```bash
# Start infrastructure services only
docker-compose up -d mysql redis nacos sentinel-dashboard zipkin elasticsearch

# Build and start all services
mvn clean package -DskipTests
docker-compose up -d

# View service logs
docker-compose logs -f {service-name}

# Stop all services
docker-compose down
```

### Service Startup Order
1. **Infrastructure**: MySQL, Redis, Nacos, Sentinel, Zipkin, Elasticsearch
2. **Gateway**: muying-mall-gateway (port 8080)
3. **Core Services**: user (8001), product (8002), order (8003), payment (8004)
4. **Support Services**: search (8005), logistics (8006), comment (8007), points (8008), admin (8009)

### Environment Profiles
- **dev**: Local development with embedded configurations
- **docker**: Docker container deployment
- **test**: Testing environment
- **prod**: Production environment

### Port Allocation
- **Gateway**: 8080
- **Services**: 8001-8009 (sequential by service)
- **Infrastructure**: MySQL (3306), Redis (6379), Nacos (8848), Sentinel (8858), Zipkin (9411), ES (9200), Kibana (5601)