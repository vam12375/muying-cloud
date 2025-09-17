# 微服务拆分实施计划

## 1. 项目结构重组

### 1.1 新的项目结构
```
muying-mall-microservices/
├── muying-mall-common/                 # 公共模块
├── muying-mall-gateway/                # 网关服务
├── muying-mall-user/                   # 用户服务
├── muying-mall-product/                # 商品服务
├── muying-mall-order/                  # 订单服务
├── muying-mall-payment/                # 支付服务
├── muying-mall-search/                 # 搜索服务
├── muying-mall-logistics/              # 物流服务
├── muying-mall-comment/                # 评论服务
├── muying-mall-points/                 # 积分服务
├── muying-mall-admin/                  # 管理服务
├── docker-compose.yml                  # 本地开发环境
├── kubernetes/                         # K8s部署文件
└── docs/                              # 文档
```

### 1.2 公共模块设计 (muying-mall-common)
```xml
<!-- 公共模块pom.xml -->
<project>
    <groupId>com.muyingmall</groupId>
    <artifactId>muying-mall-common</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <dependencies>
        <!-- Spring Boot Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <!-- 通用工具类 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        
        <!-- JSON处理 -->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
        </dependency>
        
        <!-- 校验注解 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
    </dependencies>
</project>
```

**公共模块包含**:
- 通用响应类 (Result, PageResult)
- 通用异常类
- 通用工具类 (JwtUtils, SecurityUtil等)
- 通用常量定义
- 通用DTO类

## 2. 服务拆分步骤实施

### 阶段一：基础设施搭建 (第1-2周)

#### 2.1 Nacos 部署配置
```yaml
# docker-compose-nacos.yml
version: '3.8'
services:
  nacos:
    image: nacos/nacos-server:v2.4.0
    container_name: nacos-server
    environment:
      MODE: standalone
      SPRING_DATASOURCE_PLATFORM: mysql
      MYSQL_SERVICE_HOST: mysql
      MYSQL_SERVICE_PORT: 3306
      MYSQL_SERVICE_DB_NAME: nacos
      MYSQL_SERVICE_USER: nacos
      MYSQL_SERVICE_PASSWORD: nacos123
    ports:
      - "8848:8848"
      - "9848:9848"
    depends_on:
      - mysql
    volumes:
      - ./nacos/logs:/home/nacos/logs
      - ./nacos/data:/home/nacos/data

  mysql:
    image: mysql:8.0
    container_name: nacos-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: nacos
      MYSQL_USER: nacos
      MYSQL_PASSWORD: nacos123
    ports:
      - "3307:3306"
    volumes:
      - ./mysql/data:/var/lib/mysql
      - ./mysql/init:/docker-entrypoint-initdb.d
```

#### 2.2 Gateway 服务配置
```yaml
# muying-mall-gateway/src/main/resources/application.yml
server:
  port: 8080

spring:
  application:
    name: muying-mall-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: dev
      config:
        server-addr: localhost:8848
        namespace: dev
        file-extension: yml
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        # 用户服务路由
        - id: user-service
          uri: lb://muying-mall-user
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
            
        # 商品服务路由
        - id: product-service
          uri: lb://muying-mall-product
          predicates:
            - Path=/api/products/**,/api/categories/**,/api/brands/**
          filters:
            - StripPrefix=1
            
        # 订单服务路由
        - id: order-service
          uri: lb://muying-mall-order
          predicates:
            - Path=/api/orders/**,/api/cart/**
          filters:
            - StripPrefix=1
            
        # 支付服务路由
        - id: payment-service
          uri: lb://muying-mall-payment
          predicates:
            - Path=/api/payments/**
          filters:
            - StripPrefix=1
            
        # 搜索服务路由
        - id: search-service
          uri: lb://muying-mall-search
          predicates:
            - Path=/api/search/**
          filters:
            - StripPrefix=1

# Sentinel配置
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8858
        port: 8719
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: gateway-flow-rules
            groupId: DEFAULT_GROUP
            rule-type: flow
```

#### 2.3 Sentinel 控制台部署
```bash
# 下载并启动Sentinel控制台
wget https://github.com/alibaba/Sentinel/releases/download/1.8.6/sentinel-dashboard-1.8.6.jar
java -Dserver.port=8858 -jar sentinel-dashboard-1.8.6.jar
```

### 阶段二：核心服务拆分 (第3-6周)

#### 2.4 用户服务 (muying-mall-user)
```xml
<!-- muying-mall-user/pom.xml -->
<project>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.5</version>
    </parent>
    
    <groupId>com.muyingmall</groupId>
    <artifactId>muying-mall-user</artifactId>
    <version>1.0.0</version>
    
    <properties>
        <spring-cloud-alibaba.version>2025.0.0.0-preview</spring-cloud-alibaba.version>
    </properties>
    
    <dependencies>
        <!-- Spring Cloud Alibaba -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        <!-- OpenFeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        
        <!-- 数据库相关 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>3.5.9</version>
        </dependency>
        
        <!-- 公共模块 -->
        <dependency>
            <groupId>com.muyingmall</groupId>
            <artifactId>muying-mall-common</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

```yaml
# muying-mall-user/src/main/resources/application.yml
server:
  port: 8001

spring:
  application:
    name: muying-mall-user
  profiles:
    active: dev
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/user_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: root123
    
  redis:
    host: localhost
    port: 6379
    database: 1
    
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: dev
        group: USER_GROUP
      config:
        server-addr: localhost:8848
        namespace: dev
        file-extension: yml
        group: USER_GROUP
    sentinel:
      transport:
        dashboard: localhost:8858
        port: 8719

# MyBatis Plus配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# Feign配置
feign:
  client:
    config:
      default:
        connect-timeout: 5000
        read-timeout: 10000
  sentinel:
    enabled: true

management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```

```java
// 用户服务启动类
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.muyingmall.user.mapper")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

#### 2.5 商品服务 (muying-mall-product)
配置与用户服务类似，端口为8002，数据库为product_db

#### 2.6 订单服务 (muying-mall-order)
配置与用户服务类似，端口为8003，数据库为order_db

### 阶段三：支撑服务拆分 (第7-10周)

#### 2.7 支付服务 (muying-mall-payment)
```java
// 支付服务Feign客户端
@FeignClient(name = "muying-mall-order", fallback = OrderServiceFallback.class)
public interface OrderServiceClient {
    @PostMapping("/internal/orders/{orderId}/payment-success")
    Result<Void> paymentSuccess(@PathVariable("orderId") Long orderId);
    
    @PostMapping("/internal/orders/{orderId}/payment-failed")
    Result<Void> paymentFailed(@PathVariable("orderId") Long orderId);
}

@Component
public class OrderServiceFallback implements OrderServiceClient {
    @Override
    public Result<Void> paymentSuccess(Long orderId) {
        return Result.error("订单服务暂时不可用，支付成功但无法更新订单状态");
    }
    
    @Override
    public Result<Void> paymentFailed(Long orderId) {
        return Result.error("订单服务暂时不可用，无法更新订单状态");
    }
}
```

#### 2.8 搜索服务 (muying-mall-search)
配置与其他服务类似，端口为8005，数据库为search_db

#### 2.9 物流服务 (muying-mall-logistics)
配置与其他服务类似，端口为8006，数据库为logistics_db

### 阶段四：辅助服务拆分 (第11-14周)

#### 2.10 评论服务 (muying-mall-comment)
#### 2.11 积分服务 (muying-mall-points)
#### 2.12 管理服务 (muying-mall-admin)

### 阶段五：优化与监控完善 (第15-16周)

#### 2.13 分布式事务配置 (Seata)
```yaml
# seata配置
seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: default-tx-group
  registry:
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace: dev
      group: SEATA_GROUP
  config:
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace: dev
      group: SEATA_GROUP
```

#### 2.14 链路追踪配置
```yaml
# 链路追踪配置
management:
  tracing:
    sampling:
      probability: 1.0
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
```

## 3. 数据库拆分方案

### 3.1 数据库实例规划
```sql
-- 用户数据库 (user_db)
CREATE DATABASE user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 商品数据库 (product_db)
CREATE DATABASE product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 订单数据库 (order_db)
CREATE DATABASE order_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 支付数据库 (payment_db)
CREATE DATABASE payment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 搜索数据库 (search_db)
CREATE DATABASE search_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 物流数据库 (logistics_db)
CREATE DATABASE logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 评论数据库 (comment_db)
CREATE DATABASE comment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 积分数据库 (points_db)
CREATE DATABASE points_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 管理数据库 (admin_db)
CREATE DATABASE admin_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3.2 数据迁移脚本
```bash
#!/bin/bash
# 数据迁移脚本

# 1. 备份原始数据库
mysqldump -u root -p muying_mall > backup_$(date +%Y%m%d_%H%M%S).sql

# 2. 创建新的数据库实例
mysql -u root -p < create_databases.sql

# 3. 迁移用户相关表
mysqldump -u root -p muying_mall user user_address user_account account_transaction | mysql -u root -p user_db

# 4. 迁移商品相关表
mysqldump -u root -p muying_mall product category brand product_specs product_image | mysql -u root -p product_db

# 5. 迁移订单相关表
mysqldump -u root -p muying_mall \`order\` order_item cart coupon user_coupon | mysql -u root -p order_db

# 继续其他表的迁移...
```

## 4. 部署配置

### 4.1 Docker Compose 本地开发环境
```yaml
# docker-compose.yml
version: '3.8'
services:
  # 基础设施
  nacos:
    image: nacos/nacos-server:v2.4.0
    ports:
      - "8848:8848"
    environment:
      MODE: standalone
      
  sentinel-dashboard:
    image: bladex/sentinel-dashboard:1.8.6
    ports:
      - "8858:8858"
      
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
      
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root123
      
  zipkin:
    image: openzipkin/zipkin
    ports:
      - "9411:9411"
      
  # 微服务
  gateway:
    build: ./muying-mall-gateway
    ports:
      - "8080:8080"
    depends_on:
      - nacos
      
  user-service:
    build: ./muying-mall-user
    ports:
      - "8001:8001"
    depends_on:
      - nacos
      - mysql
      - redis
      
  product-service:
    build: ./muying-mall-product
    ports:
      - "8002:8002"
    depends_on:
      - nacos
      - mysql
      
  # 其他服务...
```

### 4.2 Kubernetes 生产环境
```yaml
# k8s/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: muying-mall

---
# k8s/user-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  namespace: muying-mall
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: muying-mall/user-service:1.0.0
        ports:
        - containerPort: 8001
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: NACOS_SERVER_ADDR
          value: "nacos-service:8848"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"

---
apiVersion: v1
kind: Service
metadata:
  name: user-service
  namespace: muying-mall
spec:
  selector:
    app: user-service
  ports:
  - port: 8001
    targetPort: 8001
  type: ClusterIP
```

## 5. 监控配置

### 5.1 Prometheus + Grafana
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-boot-apps'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: 
        - 'user-service:8001'
        - 'product-service:8002'
        - 'order-service:8003'
        - 'payment-service:8004'
        - 'search-service:8005'
        - 'logistics-service:8006'
        - 'comment-service:8007'
        - 'points-service:8008'
        - 'admin-service:8009'
```

## 6. 测试策略

### 6.1 单元测试
每个微服务独立的单元测试，覆盖率要求80%以上

### 6.2 集成测试
使用TestContainers进行集成测试

### 6.3 端到端测试
使用Postman/Newman进行API测试

### 6.4 性能测试
使用JMeter进行压力测试

## 7. 发布策略

### 7.1 CI/CD流水线
```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 21
      uses: actions/setup-java@v2
      with:
        java-version: '21'
    - name: Run tests
      run: mvn test
      
  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Build Docker images
      run: |
        docker build -t muying-mall/user-service:${{ github.sha }} ./muying-mall-user
        docker build -t muying-mall/product-service:${{ github.sha }} ./muying-mall-product
        # 其他服务镜像构建...
        
  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
    - name: Deploy to Kubernetes
      run: |
        kubectl apply -f k8s/
        kubectl set image deployment/user-service user-service=muying-mall/user-service:${{ github.sha }} -n muying-mall
```

### 7.2 蓝绿部署
使用Kubernetes的Deployment rolling update实现蓝绿部署

### 7.3 灰度发布
使用Istio或Spring Cloud Gateway实现基于权重的流量分发

## 8. 风险评估与应对

### 8.1 数据一致性风险
- 使用分布式事务(Seata)保证强一致性
- 使用事件驱动架构保证最终一致性
- 建立数据补偿机制

### 8.2 服务依赖风险
- 实现熔断降级机制
- 设计合理的超时和重试策略
- 准备服务降级方案

### 8.3 性能风险
- 建立完善的监控体系
- 实施压力测试
- 准备扩容方案

### 8.4 运维复杂性
- 自动化部署流程
- 标准化日志格式
- 建立故障预案

这个详细的拆分实施计划为微服务化提供了完整的路线图，确保项目能够平稳、有序地进行微服务架构改造。