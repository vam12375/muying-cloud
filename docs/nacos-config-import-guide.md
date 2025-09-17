# Nacos 3.0.2 配置导入指南

## 1. Nacos配置说明

本指南提供了母婴商城微服务系统在Nacos 3.0.2中的完整配置，包括所有微服务的配置文件和必要的基础设施配置。

## 2. 访问Nacos控制台

- **访问地址**: http://localhost:8848/nacos
- **用户名**: nacos
- **密码**: nacos

## 3. 配置导入步骤

### 3.1 进入配置管理
1. 登录Nacos控制台
2. 点击左侧菜单"配置管理" → "配置列表"
3. 点击右上角"+"按钮或"导入配置"

### 3.2 命名空间配置
建议创建专门的命名空间来管理微服务配置：

**命名空间ID**: muying-mall
**命名空间名**: 母婴商城微服务

## 4. 微服务配置列表

以下是需要在Nacos中创建的配置文件：

### 4.1 网关服务配置
**Data ID**: muying-mall-gateway-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
server:
  port: 8080

spring:
  application:
    name: muying-mall-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos
      config:
        server-addr: localhost:8848
        username: nacos
        password: nacos
        file-extension: yml
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: user-service
          uri: lb://muying-mall-user
          predicates:
            - Path=/api/users/**
        - id: product-service
          uri: lb://muying-mall-product
          predicates:
            - Path=/api/products/**,/api/categories/**,/api/brands/**
        - id: order-service
          uri: lb://muying-mall-order
          predicates:
            - Path=/api/orders/**,/api/cart/**
        - id: payment-service
          uri: lb://muying-mall-payment
          predicates:
            - Path=/api/payments/**
        - id: search-service
          uri: lb://muying-mall-search
          predicates:
            - Path=/api/search/**
        - id: logistics-service
          uri: lb://muying-mall-logistics
          predicates:
            - Path=/api/logistics/**
        - id: comment-service
          uri: lb://muying-mall-comment
          predicates:
            - Path=/api/comments/**
        - id: points-service
          uri: lb://muying-mall-points
          predicates:
            - Path=/api/points/**
        - id: admin-service
          uri: lb://muying-mall-admin
          predicates:
            - Path=/api/admin/**

management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
```

### 4.2 公共数据源配置
**Data ID**: muying-mall-datasource.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  datasource:
    username: root
    password: admin
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 600000
      max-lifetime: 1800000
      connection-test-query: SELECT 1

  redis:
    host: localhost
    port: 6379
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    publisher-confirm-type: correlated
    publisher-returns: true
```

### 4.3 用户服务配置
**Data ID**: muying-mall-user-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-user
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_user?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8001

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.4 商品服务配置
**Data ID**: muying-mall-product-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-product
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_product?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8002

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.5 订单服务配置
**Data ID**: muying-mall-order-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-order
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_order?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8003

feign:
  client:
    config:
      default:
        connect-timeout: 5000
        read-timeout: 10000

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.6 支付服务配置
**Data ID**: muying-mall-payment-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-payment
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_payment?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8004

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.7 搜索服务配置
**Data ID**: muying-mall-search-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-search
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_search?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos
  elasticsearch:
    uris: http://localhost:9200

server:
  port: 8005

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.8 物流服务配置
**Data ID**: muying-mall-logistics-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-logistics
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_logistics?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8006

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.9 评论服务配置
**Data ID**: muying-mall-comment-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-comment
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_comment?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8007

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.10 积分服务配置
**Data ID**: muying-mall-points-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-points
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_points?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8008

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4.11 管理服务配置
**Data ID**: muying-mall-admin-dev.yml
**Group**: DEFAULT_GROUP
**配置格式**: YAML

```yaml
spring:
  application:
    name: muying-mall-admin
  datasource:
    url: jdbc:mysql://localhost:3306/muying_mall_admin?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos

server:
  port: 8009

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

## 5. 快速导入说明

### 5.1 使用Nacos控制台导入
1. 将上述每个配置复制到对应的Data ID配置中
2. 确保Group设置为DEFAULT_GROUP
3. 配置格式选择YAML
4. 点击发布

### 5.2 配置验证
导入完成后，可以在"配置列表"中看到所有配置文件：
- muying-mall-gateway-dev.yml
- muying-mall-datasource.yml
- muying-mall-user-dev.yml
- muying-mall-product-dev.yml
- muying-mall-order-dev.yml
- muying-mall-payment-dev.yml
- muying-mall-search-dev.yml
- muying-mall-logistics-dev.yml
- muying-mall-comment-dev.yml
- muying-mall-points-dev.yml
- muying-mall-admin-dev.yml

## 6. 注意事项

1. **数据库配置**: 确保MySQL用户名为root，密码为admin
2. **Nacos版本**: 必须使用Nacos 3.0.2版本
3. **端口配置**: 确保8001-8009端口未被占用
4. **Redis配置**: 默认连接本地Redis，无密码
5. **RabbitMQ配置**: 使用默认guest/guest账号
6. **Elasticsearch配置**: 搜索服务需要Elasticsearch支持

## 7. 配置生效

配置导入完成后，启动微服务时会自动从Nacos读取配置。如需修改配置，直接在Nacos控制台修改即可，支持动态刷新。