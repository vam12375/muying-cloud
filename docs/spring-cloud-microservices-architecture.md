# 母婴商城微服务架构设计文档

## 1. 项目概述

### 1.1 当前系统分析
母婴商城系统是一个基于Spring Boot 3.5.5的单体应用，包含以下主要功能模块：
- 用户管理系统
- 商品管理系统
- 订单管理系统
- 支付系统
- 物流管理系统
- 评论系统
- 积分系统
- 搜索系统
- 管理后台系统

### 1.2 微服务化目标
将单体应用拆分为多个独立的微服务，采用Spring Cloud Alibaba技术栈实现服务治理。

## 2. 技术栈选型

### 2.1 基础框架
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2025.0.0 (Northfields)
- **Spring Cloud Alibaba**: 2025.0.0.0-preview
- **JDK**: 21

### 2.2 核心组件
- **注册中心**: Nacos 3.0.2
- **配置中心**: Nacos Config 3.0.2
- **负载均衡**: Spring Cloud LoadBalancer (替代Ribbon)
- **服务调用**: OpenFeign
- **服务保护**: Sentinel 1.8.x
- **服务网关**: Spring Cloud Gateway
- **消息队列**: RabbitMQ 3.12.x
- **分布式事务**: Seata
- **链路追踪**: Micrometer Tracing + Zipkin

### 2.3 数据存储
- **数据库**: MySQL 8.0 (用户: root, 密码: admin)
- **缓存**: Redis 7.x
- **搜索引擎**: Elasticsearch 8.11.0
- **对象存储**: 阿里云OSS (可选)

## 3. 微服务拆分设计

### 3.1 服务拆分原则
- **业务领域划分**: 基于DDD领域驱动设计
- **数据库分离**: 每个微服务拥有独立的数据库
- **单一职责**: 每个服务专注于特定业务功能
- **高内聚低耦合**: 服务内部功能相关性强，服务间依赖最小化

### 3.2 微服务架构图

```
                                    ┌─────────────────┐
                                    │   用户端应用     │
                                    │  (Web/Mobile)   │
                                    └─────────┬───────┘
                                              │
                                    ┌─────────▼───────┐
                                    │  Spring Cloud   │
                                    │    Gateway      │
                                    │   API网关       │
                                    └─────────┬───────┘
                                              │
                        ┌─────────────────────┼─────────────────────┐
                        │                     │                     │
            ┌───────────▼──────────┐ ┌──────▼──────┐ ┌──────────▼──────────┐
            │    用户服务          │ │   商品服务   │ │      订单服务        │
            │  user-service       │ │product-service│ │   order-service     │
            └─────────────────────┘ └─────────────┘ └─────────────────────┘
                        │                     │                     │
            ┌───────────▼──────────┐ ┌──────▼──────┐ ┌──────────▼──────────┐
            │    支付服务          │ │   搜索服务   │ │      物流服务        │
            │ payment-service     │ │search-service │ │ logistics-service   │
            └─────────────────────┘ └─────────────┘ └─────────────────────┘
                        │                     │                     │
            ┌───────────▼──────────┐ ┌──────▼──────┐ ┌──────────▼──────────┐
            │    评论服务          │ │   积分服务   │ │      管理服务        │
            │ comment-service     │ │points-service │ │   admin-service     │
            └─────────────────────┘ └─────────────┘ └─────────────────────┘
                                              │
                                    ┌─────────▼───────┐
                                    │      Nacos      │
                                    │   注册/配置中心  │
                                    └─────────────────┘
```

### 3.3 微服务详细分解

#### 3.3.1 用户服务 (user-service)
**职责**: 用户注册、登录、个人信息管理、地址管理
**端口**: 8001
**数据库**: user_db
**核心实体**:
- User (用户)
- UserAddress (用户地址)
- UserAccount (用户账户)

**主要API**:
- POST /api/users/register - 用户注册
- POST /api/users/login - 用户登录
- GET /api/users/profile - 获取用户信息
- PUT /api/users/profile - 更新用户信息
- GET/POST/PUT/DELETE /api/users/addresses - 地址管理

#### 3.3.2 商品服务 (product-service)
**职责**: 商品信息管理、库存管理、分类品牌管理
**端口**: 8002
**数据库**: product_db
**核心实体**:
- Product (商品)
- Category (分类)
- Brand (品牌)
- ProductSpecs (商品规格)
- ProductImage (商品图片)

**主要API**:
- GET /api/products - 商品列表
- GET /api/products/{id} - 商品详情
- POST/PUT/DELETE /api/products - 商品管理 (管理员)
- GET /api/categories - 分类列表
- GET /api/brands - 品牌列表

#### 3.3.3 订单服务 (order-service)
**职责**: 订单创建、订单状态管理、购物车管理
**端口**: 8003
**数据库**: order_db
**核心实体**:
- Order (订单)
- OrderItem (订单项)
- Cart (购物车)
- Coupon (优惠券)
- UserCoupon (用户优惠券)

**主要API**:
- POST /api/orders - 创建订单
- GET /api/orders - 订单列表
- GET /api/orders/{id} - 订单详情
- PUT /api/orders/{id}/status - 更新订单状态
- GET/POST/PUT/DELETE /api/cart - 购物车管理

#### 3.3.4 支付服务 (payment-service)
**职责**: 支付处理、支付回调、钱包管理
**端口**: 8004
**数据库**: payment_db
**核心实体**:
- Payment (支付记录)
- PaymentStateLog (支付状态日志)
- AccountTransaction (账户交易)

**主要API**:
- POST /api/payments - 创建支付
- POST /api/payments/alipay/notify - 支付宝回调
- POST /api/payments/wechat/notify - 微信回调
- GET /api/payments/{id} - 支付详情

#### 3.3.5 搜索服务 (search-service)
**职责**: 商品搜索、搜索统计、搜索推荐
**端口**: 8005
**数据库**: search_db + Elasticsearch
**核心实体**:
- ProductDocument (商品文档)
- SearchStatistics (搜索统计)

**主要API**:
- GET /api/search/products - 商品搜索
- GET /api/search/suggestions - 搜索建议
- GET /api/search/statistics - 搜索统计

#### 3.3.6 物流服务 (logistics-service)
**职责**: 物流信息管理、配送跟踪
**端口**: 8006
**数据库**: logistics_db
**核心实体**:
- Logistics (物流)
- LogisticsCompany (物流公司)

**主要API**:
- GET /api/logistics/{orderNo} - 物流信息
- POST /api/logistics - 创建物流
- PUT /api/logistics/{id} - 更新物流状态

#### 3.3.7 评论服务 (comment-service)
**职责**: 商品评论、评论回复、评论统计
**端口**: 8007
**数据库**: comment_db
**核心实体**:
- Comment (评论)
- CommentReply (评论回复)
- CommentTag (评论标签)

**主要API**:
- GET /api/comments/product/{productId} - 商品评论
- POST /api/comments - 发表评论
- POST /api/comments/{id}/reply - 回复评论

#### 3.3.8 积分服务 (points-service)
**职责**: 积分管理、积分兑换、积分统计
**端口**: 8008
**数据库**: points_db
**核心实体**:
- UserPoints (用户积分)
- PointsHistory (积分历史)
- PointsProduct (积分商品)
- PointsRule (积分规则)

**主要API**:
- GET /api/points/balance - 积分余额
- POST /api/points/earn - 积分获取
- POST /api/points/exchange - 积分兑换
- GET /api/points/history - 积分历史

#### 3.3.9 管理服务 (admin-service)
**职责**: 管理员功能、系统监控、数据统计
**端口**: 8009
**数据库**: admin_db
**核心实体**:
- AdminLoginRecord (管理员登录记录)
- AdminOperationLog (管理员操作日志)

**主要API**:
- POST /api/admin/login - 管理员登录
- GET /api/admin/dashboard - 仪表板数据
- GET /api/admin/statistics - 系统统计

#### 3.3.10 网关服务 (gateway-service)
**职责**: 路由转发、认证授权、限流熔断
**端口**: 8080

## 4. 基础设施组件

### 4.1 Nacos 配置
**版本**: 3.0.2
**端口**: 8848
**功能**:
- 服务注册与发现
- 配置管理
- 服务健康检查

**启动命令**:
```bash
# 下载 Nacos 3.0.2
wget https://github.com/alibaba/nacos/releases/download/3.0.2/nacos-server-3.0.2.tar.gz
tar -xzf nacos-server-3.0.2.tar.gz
cd nacos/bin

# Windows启动
startup.cmd -m standalone

# Linux/Mac启动
sh startup.sh -m standalone
```

**访问地址**: http://localhost:8848/nacos
**默认账号**: nacos/nacos

### 4.2 Sentinel 配置
**版本**: 1.8.x
**控制台端口**: 8858
**功能**:
- 流量控制
- 熔断降级
- 系统负载保护

### 4.3 链路追踪
**组件**: Micrometer Tracing + Zipkin
**Zipkin端口**: 9411

## 5. 部署架构

### 5.1 容器化部署
- 每个微服务独立Docker容器
- 使用Docker Compose进行本地开发
- Kubernetes进行生产环境部署

### 5.2 数据库设计
- 每个微服务独立数据库实例
- 读写分离配置
- 数据库连接池配置

### 5.3 缓存策略
- Redis集群配置
- 分布式缓存方案
- 缓存一致性保证

## 6. 跨服务通信

### 6.1 同步调用
- OpenFeign进行服务间调用
- 负载均衡策略配置
- 超时和重试机制

### 6.2 异步通信
- RabbitMQ消息队列
- 事件驱动架构
- 消息可靠性保证

**RabbitMQ配置**:
```bash
# 安装RabbitMQ (需要先安装Erlang)
# Windows: 下载安装包
# Ubuntu: sudo apt-get install rabbitmq-server
# CentOS: sudo yum install rabbitmq-server

# 启动RabbitMQ
rabbitmq-server

# 启用管理插件
rabbitmq-plugins enable rabbitmq_management
```

**访问地址**: http://localhost:15672
**默认账号**: guest/guest
**AMQP端口**: 5672

### 6.3 分布式事务
- Seata分布式事务解决方案
- AT、TCC、SAGA模式选择
- 数据一致性保证

## 7. 安全设计

### 7.1 认证授权
- JWT Token机制
- OAuth2.0集成
- 权限控制

### 7.2 网关安全
- API限流
- 黑白名单
- 参数校验

## 8. 监控与运维

### 8.1 应用监控
- Spring Boot Actuator
- Micrometer metrics
- 自定义监控指标

### 8.2 日志管理
- 集中式日志收集
- 日志标准化
- 日志分析

### 8.3 告警机制
- 服务健康检查
- 异常告警
- 性能监控告警

## 9. 迁移策略

### 9.1 渐进式迁移
1. **阶段一**: 基础设施搭建 (Nacos, Gateway, Sentinel)
2. **阶段二**: 核心服务拆分 (用户、商品、订单)
3. **阶段三**: 支撑服务拆分 (支付、物流、搜索)
4. **阶段四**: 辅助服务拆分 (评论、积分、管理)
5. **阶段五**: 优化与监控完善

### 9.2 数据迁移
- 数据库拆分脚本
- 数据一致性校验
- 回滚方案准备

### 9.3 风险控制
- 蓝绿部署
- 灰度发布
- 回滚机制

## 11. 本地开发环境启动指南

### 11.1 基础设施启动顺序

**第一步：启动MySQL**
```bash
# 确保MySQL 8.0已安装并启动
# 用户名: root
# 密码: admin
# 端口: 3306

# 创建数据库
mysql -u root -padmin
CREATE DATABASE muying_mall_user;
CREATE DATABASE muying_mall_product;
CREATE DATABASE muying_mall_order;
CREATE DATABASE muying_mall_payment;
CREATE DATABASE muying_mall_search;
CREATE DATABASE muying_mall_logistics;
CREATE DATABASE muying_mall_comment;
CREATE DATABASE muying_mall_points;
CREATE DATABASE muying_mall_admin;
```

**第二步：启动Redis**
```bash
# 启动Redis
redis-server
# 默认端口: 6379
```

**第三步：启动RabbitMQ**
```bash
# 启动RabbitMQ
rabbitmq-server
# AMQP端口: 5672
# 管理界面: http://localhost:15672 (guest/guest)
```

**第四步：启动Nacos 3.0.2**
```bash
# 解压并启动Nacos
cd nacos/bin
# Windows
startup.cmd -m standalone
# Linux/Mac
sh startup.sh -m standalone
# 访问: http://localhost:8848/nacos (nacos/nacos)
```

**第五步：启动Elasticsearch (可选)**
```bash
# 启动Elasticsearch
bin/elasticsearch
# 端口: 9200
```

### 11.2 微服务启动

**Windows环境:**
```batch
# 在 muying-mall-microservices 目录下执行
start-all-services.bat
```

**Linux/Mac环境:**
```bash
# 在 muying-mall-microservices 目录下执行
chmod +x start-all-services.sh
./start-all-services.sh

# 停止所有服务
./stop-all-services.sh
```

### 11.3 服务验证
启动完成后可通过以下地址验证服务状态:
- 网关服务: http://localhost:8080/actuator/health
- Nacos控制台: http://localhost:8848/nacos
- RabbitMQ管理界面: http://localhost:15672

### 11.4 常见问题解决
1. **端口冲突**: 检查8080-8009端口是否被占用
2. **数据库连接失败**: 确认MySQL用户名密码为root/admin
3. **Nacos连接失败**: 确认Nacos已启动且版本为3.0.2
4. **RabbitMQ连接失败**: 确认RabbitMQ服务已启动

## 10. 总结

通过微服务架构改造，母婴商城系统将获得：
- **更好的可扩展性**: 独立扩展各个服务
- **更高的可维护性**: 服务边界清晰，职责单一
- **更强的容错性**: 服务隔离，避免级联失败
- **更快的开发效率**: 团队并行开发，技术栈多样化
- **更灵活的部署**: 独立部署，版本管理灵活

该架构设计充分考虑了Spring Boot 3.5.5和Spring Cloud Alibaba的最新特性，为系统的长期发展奠定了坚实的基础。