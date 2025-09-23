# 母婴商城微服务架构

基于Spring Boot 3.5.5和Spring Cloud Alibaba的微服务架构项目。

## 项目结构

```
muying-mall-microservices/
├── muying-mall-common/          # 公共模块
├── muying-mall-gateway/         # 网关服务 (8080)
├── muying-mall-user/           # 用户服务 (8001)
├── muying-mall-product/        # 商品服务 (8002)
├── muying-mall-order/          # 订单服务 (8003)
├── muying-mall-payment/        # 支付服务 (8004)
├── muying-mall-search/         # 搜索服务 (8005)
├── muying-mall-logistics/      # 物流服务 (8006)
├── muying-mall-comment/        # 评论服务 (8007)
├── muying-mall-points/         # 积分服务 (8008)
├── muying-mall-admin/          # 管理服务 (8009)
├── docker-compose.yml          # Docker编排文件
├── pom.xml                     # 父项目POM
└── README.md                   # 项目说明
```

## 技术栈

### 基础框架
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2025.0.0 (Northfields)
- **Spring Cloud Alibaba**: 2025.0.0.0-preview
- **JDK**: 21

### 核心组件
- **注册中心**: Nacos 3.0.2
- **配置中心**: Nacos Config
- **负载均衡**: Spring Cloud LoadBalancer
- **服务调用**: OpenFeign
- **服务保护**: Sentinel 1.8.x
- **服务网关**: Spring Cloud Gateway
- **链路追踪**: Micrometer Tracing + Zipkin

### 数据存储
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.x
- **搜索引擎**: Elasticsearch 8.11.0

## 快速开始

### 前置要求
- JDK 21+
- Maven 3.9.9
- Docker & Docker Compose

### 本地开发环境启动

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd muying-mall-microservices
   ```

2. **构建项目**
   ```bash
   mvn clean compile
   ```

3. **启动基础设施**
   ```bash
   docker-compose up -d mysql redis nacos sentinel-dashboard zipkin elasticsearch
   ```

4. **等待Nacos启动完成** (大约30-60秒)
   访问 http://localhost:8848/nacos 确认启动成功
   默认用户名/密码: nacos/nacos

5. **启动网关服务**
   ```bash
   cd muying-mall-gateway
   mvn spring-boot:run
   ```

6. **启动用户服务**
   ```bash
   cd muying-mall-user  
   mvn spring-boot:run
   ```

### Docker环境启动

1. **构建并启动所有服务**
   ```bash
   mvn clean package -DskipTests
   docker-compose up -d
   ```

2. **查看服务状态**
   ```bash
   docker-compose ps
   ```

## 服务端口

| 服务名称              | 端口   | 描述    |
|-------------------|------|-------|
| Gateway           | 8080 | API网关 |
| User Service      | 8001 | 用户服务  |
| Product Service   | 8002 | 商品服务  |
| Order Service     | 8003 | 订单服务  |
| Payment Service   | 8004 | 支付服务  |
| Search Service    | 8005 | 搜索服务  |
| Logistics Service | 8006 | 物流服务  |
| Comment Service   | 8007 | 评论服务  |
| Points Service    | 8008 | 积分服务  |
| Admin Service     | 8009 | 管理服务  |

## 基础设施端口

| 服务名称          | 端口   | 用户名/密码       |
|---------------|------|--------------|
| MySQL         | 3306 | root/root123 |
| Redis         | 6379 | -            |
| Nacos         | 8848 | nacos/nacos  |
| Sentinel      | 8858 | -            |
| Zipkin        | 9411 | -            |
| Elasticsearch | 9200 | -            |
| Kibana        | 5601 | -            |

## API文档

启动服务后，可以通过以下地址访问API文档：

- 网关服务: http://localhost:8080/swagger-ui.html
- 用户服务: http://localhost:8001/swagger-ui.html

## 监控面板

- **Nacos控制台**: http://localhost:8848/nacos
- **Sentinel控制台**: http://localhost:8858
- **Zipkin链路追踪**: http://localhost:9411
- **Kibana**: http://localhost:5601

## 数据库

系统使用多个独立的数据库实例：
- `user_db` - 用户服务数据库
- `product_db` - 商品服务数据库  
- `order_db` - 订单服务数据库
- `payment_db` - 支付服务数据库
- `search_db` - 搜索服务数据库
- `logistics_db` - 物流服务数据库
- `comment_db` - 评论服务数据库
- `points_db` - 积分服务数据库
- `admin_db` - 管理服务数据库
- `nacos` - Nacos配置数据库

## 开发指南

### 添加新的微服务

1. 在父项目下创建新模块
2. 配置pom.xml继承父项目
3. 添加必要的依赖
4. 配置application.yml
5. 创建主启动类
6. 更新docker-compose.yml

### 服务间调用

使用OpenFeign进行服务间调用：

```java
@FeignClient(name = "muying-mall-user", fallback = UserServiceFallback.class)
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    Result<UserInfo> getUserInfo(@PathVariable("id") Integer id);
}
```

### 配置中心

在Nacos中统一管理配置：
- Data ID: {spring.application.name}-{profile}.{file-extension}
- Group: 对应的服务组
- 配置格式: yaml

### 限流降级

使用Sentinel进行流量控制：
- 在Sentinel控制台配置规则
- 使用@SentinelResource注解标记资源
- 实现降级回调方法

## 部署

### 生产环境部署

1. **构建镜像**
   ```bash
   mvn clean package -DskipTests
   docker build -t muying-mall/gateway:1.0.0 ./muying-mall-gateway
   docker build -t muying-mall/user-service:1.0.0 ./muying-mall-user
   ```

2. **推送到镜像仓库**
   ```bash
   docker push muying-mall/gateway:1.0.0
   docker push muying-mall/user-service:1.0.0
   ```

3. **Kubernetes部署**
   ```bash
   kubectl apply -f k8s/
   ```

## 故障排除

### 常见问题

1. **Nacos连接失败**
   - 检查Nacos服务是否启动
   - 确认网络连接
   - 验证配置地址

2. **数据库连接失败**
   - 检查MySQL服务状态
   - 验证数据库连接参数
   - 确认数据库是否创建

3. **Redis连接失败**
   - 检查Redis服务状态
   - 验证连接配置

### 日志查看

```bash
# 查看容器日志
docker-compose logs -f gateway
docker-compose logs -f user-service

# 查看特定时间段日志
docker-compose logs -f --since="2024-01-01T00:00:00" gateway
```

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

如有问题请提交 Issue 或联系项目维护者。