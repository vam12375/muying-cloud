# 🛍️ 母婴商城微服务架构

<div align="center">

![版本](https://img.shields.io/badge/版本-1.0.0-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.0.0-orange)
![Java](https://img.shields.io/badge/JDK-21-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Redis](https://img.shields.io/badge/Redis-7.x-red)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.11.0-yellow)
![Nacos](https://img.shields.io/badge/Nacos-3.0.2-green)

</div>

基于Spring Boot 3.5.5和Spring Cloud Alibaba的微服务架构母婴电商平台，采用最新Java 21技术栈，提供完整的电商业务解决方案。

## 📋 项目概述

本项目是一个完整的母婴商城系统，基于微服务架构设计，包含用户、商品、订单、支付、搜索、物流、评论、积分和管理后台等核心功能模块，实现了微服务架构下的电商全流程业务处理。

### 🏗️ 技术架构

![技术架构](docs/images/architecture.png)

## 🧩 项目模块

```bash
muying-cloud/
├── muying-mall-common/          # 公共模块 - 工具类、通用配置
├── muying-mall-gateway/         # 网关服务 - 统一入口、鉴权、路由 (8080)
├── muying-mall-user/            # 用户服务 - 注册、登录、信息管理 (8001)
├── muying-mall-product/         # 商品服务 - 商品、分类、品牌管理 (8002)
├── muying-mall-order/           # 订单服务 - 订单创建、状态管理 (8003)
├── muying-mall-payment/         # 支付服务 - 支付宝、微信支付集成 (8004)
├── muying-mall-search/          # 搜索服务 - 商品搜索、推荐 (8005)
├── muying-mall-logistics/       # 物流服务 - 物流信息、轨迹管理 (8006)
├── muying-mall-comment/         # 评论服务 - 商品评价、回复管理 (8007)
├── muying-mall-points/          # 积分服务 - 积分账户、商品兑换 (8008)
├── muying-mall-admin/           # 管理服务 - 后台管理、统计 (8009)
└── docker/                      # Docker配置文件
```

## 🚀 核心技术栈

### 基础框架
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2025.0.0 
- **Spring Cloud Alibaba**: 2023.0.1.2
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
- **数据库**: MySQL 8.0.33
- **ORM框架**: MyBatis Plus 3.5.9
- **缓存**: Redis 7.x
- **搜索引擎**: Elasticsearch 8.11.0

## ✨ 主要功能

<table>
  <tr>
    <td width="50%">
      <h3>🧑‍🤝‍🧑 用户服务</h3>
      <ul>
        <li>用户注册、登录、认证</li>
        <li>用户信息管理</li>
        <li>地址管理</li>
        <li>收藏管理</li>
        <li>钱包管理</li>
      </ul>
    </td>
    <td width="50%">
      <h3>🛒 商品服务</h3>
      <ul>
        <li>商品信息管理</li>
        <li>商品分类管理</li>
        <li>品牌管理</li>
        <li>库存管理</li>
        <li>规格参数管理</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>
      <h3>📝 订单服务</h3>
      <ul>
        <li>订单创建与管理</li>
        <li>购物车管理</li>
        <li>订单状态流转</li>
        <li>订单查询统计</li>
      </ul>
    </td>
    <td>
      <h3>💰 支付服务</h3>
      <ul>
        <li>支付宝支付集成</li>
        <li>微信支付集成</li>
        <li>钱包支付</li>
        <li>退款处理</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>
      <h3>🔍 搜索服务</h3>
      <ul>
        <li>商品搜索</li>
        <li>搜索建议</li>
        <li>热门词汇</li>
        <li>相似商品推荐</li>
      </ul>
    </td>
    <td>
      <h3>🚚 物流服务</h3>
      <ul>
        <li>物流信息管理</li>
        <li>物流轨迹跟踪</li>
        <li>物流公司管理</li>
        <li>第三方物流接入</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>
      <h3>💬 评论服务</h3>
      <ul>
        <li>商品评价管理</li>
        <li>评价回复</li>
        <li>评价标签</li>
        <li>评价统计</li>
      </ul>
    </td>
    <td>
      <h3>🎁 积分服务</h3>
      <ul>
        <li>积分账户管理</li>
        <li>积分商品管理</li>
        <li>积分规则配置</li>
        <li>积分商品兑换</li>
      </ul>
    </td>
  </tr>
</table>

## 🛠️ 快速开始

### 环境要求
- JDK 21+
- Maven 3.9.9+
- Docker & Docker Compose
- Git

### 本地开发环境启动

1. **克隆项目**
```bash
git clone https://github.com/yourusername/muying-cloud.git
cd muying-cloud
```

2. **构建项目**
```bash
mvn clean compile
```

3. **启动基础设施**
```bash
docker-compose up -d mysql redis nacos sentinel-dashboard zipkin elasticsearch
```

4. **启动服务**

等待Nacos启动完成 (约30-60秒)，访问 http://localhost:8848/nacos 确认启动成功 (用户名/密码: nacos/nacos)

```bash
# 启动网关服务
cd muying-mall-gateway
mvn spring-boot:run

# 启动用户服务
cd ../muying-mall-user
mvn spring-boot:run

# 启动其他服务...
```

### Docker环境一键启动

```bash
# 构建并启动所有服务
mvn clean package -DskipTests
docker-compose up -d

# 查看服务状态
docker-compose ps
```

## 📊 服务端口

| 服务名称 | 端口 | 描述 |
|---------|-----|------|
| Gateway | 8080 | API网关 |
| User | 8001 | 用户服务 |
| Product | 8002 | 商品服务 |
| Order | 8003 | 订单服务 |
| Payment | 8004 | 支付服务 |
| Search | 8005 | 搜索服务 |
| Logistics | 8006 | 物流服务 |
| Comment | 8007 | 评论服务 |
| Points | 8008 | 积分服务 |
| Admin | 8009 | 管理服务 |

## 🔧 基础设施

| 服务名称 | 端口 | 用户名/密码 | 说明 |
|---------|-----|------------|------|
| MySQL | 3306 | root/root123 | 数据库 |
| Redis | 6379 | - | 缓存 |
| Nacos | 8848 | nacos/nacos | 注册/配置中心 |
| Sentinel | 8858 | - | 限流熔断 |
| Zipkin | 9411 | - | 链路追踪 |
| Elasticsearch | 9200 | - | 搜索引擎 |
| Kibana | 5601 | - | ES可视化 |

## 📝 API文档

启动服务后，可以通过以下地址访问API文档：

- 网关服务: http://localhost:8080/swagger-ui.html
- 用户服务: http://localhost:8001/swagger-ui.html
- 商品服务: http://localhost:8002/swagger-ui.html
- 其他服务: http://localhost:{port}/swagger-ui.html

## 📈 监控面板

- **Nacos控制台**: http://localhost:8848/nacos
- **Sentinel控制台**: http://localhost:8858
- **Zipkin链路追踪**: http://localhost:9411
- **Kibana**: http://localhost:5601

## 🧑‍💻 开发指南

### 服务间调用示例

使用OpenFeign进行服务间调用：

```java
@FeignClient(name = "muying-mall-user", fallback = UserServiceFallback.class)
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    Result<UserInfo> getUserInfo(@PathVariable("id") Integer id);
}
```

### 配置中心使用示例

在Nacos中统一管理配置：
- Data ID: {spring.application.name}-{profile}.{file-extension}
- Group: 对应的服务组
- 配置格式: yaml

### 限流降级示例

```java
@SentinelResource(
    value = "getUserInfo",
    blockHandler = "getUserInfoBlockHandler",
    fallback = "getUserInfoFallback"
)
public Result<UserInfo> getUserInfo(Integer userId) {
    // 业务逻辑
}

// 限流处理
public Result<UserInfo> getUserInfoBlockHandler(Integer userId, BlockException ex) {
    log.error("获取用户信息被限流", ex);
    return Result.failed("系统繁忙，请稍后再试");
}

// 熔断降级处理
public Result<UserInfo> getUserInfoFallback(Integer userId, Throwable ex) {
    log.error("获取用户信息异常", ex);
    return Result.failed("服务暂时不可用");
}
```

## 🚢 部署

### Docker部署

```bash
# 构建镜像
mvn clean package -DskipTests
docker build -t muying-mall/gateway:1.0.0 ./muying-mall-gateway
docker build -t muying-mall/user-service:1.0.0 ./muying-mall-user
# ...其他服务

# 启动服务
docker-compose up -d
```

### Kubernetes部署

```bash
# 部署基础设施
kubectl apply -f k8s/infrastructure/

# 部署服务
kubectl apply -f k8s/services/
```

## ❓ 常见问题排查

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

4. **服务注册失败**
   - 检查服务配置中的Nacos地址
   - 确认服务命名规范
   - 查看注册日志

5. **查看服务日志**
```bash
# 查看容器日志
docker-compose logs -f gateway
docker-compose logs -f user-service

# 查看特定时间段日志
docker-compose logs -f --since="2025-09-01T00:00:00" gateway
```

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📮 联系方式

如有问题请提交 Issue 或联系项目维护者。
