# Nacos配置说明文档

## 概述

所有微服务模块已经统一配置了Nacos作为服务注册中心和配置中心。本文档详细说明了配置的内容和使用方式。

## 已配置的模块

1. **muying-mall-gateway** (端口: 8080) - 网关服务
2. **muying-mall-user** (端口: 8001) - 用户服务
3. **muying-mall-product** (端口: 8002) - 商品服务
4. **muying-mall-order** (端口: 8003) - 订单服务
5. **muying-mall-payment** (端口: 8004) - 支付服务
6. **muying-mall-search** (端口: 8005) - 搜索服务
7. **muying-mall-logistics** (端口: 8006) - 物流服务
8. **muying-mall-comment** (端口: 8007) - 评论服务
9. **muying-mall-points** (端口: 8008) - 积分服务
10. **muying-mall-admin** (端口: 8009) - 管理服务

## Nacos配置详情

### 服务发现配置
- **服务器地址**: `localhost:8848`
- **命名空间**: `dev`
- **认证信息**: username: `nacos`, password: `nacos`
- **服务分组**: 每个服务使用独立的分组
  - GATEWAY_GROUP (网关)
  - USER_GROUP (用户服务)
  - PRODUCT_GROUP (商品服务)
  - ORDER_GROUP (订单服务)
  - PAYMENT_GROUP (支付服务)
  - SEARCH_GROUP (搜索服务)
  - LOGISTICS_GROUP (物流服务)
  - COMMENT_GROUP (评论服务)
  - POINTS_GROUP (积分服务)
  - ADMIN_GROUP (管理服务)

### 配置中心设置
- **文件扩展名**: `yml`
- **配置自动刷新**: 支持
- **共享配置**: 所有服务共享 `muying-mall-common.yml` 配置

### 网关路由配置
网关已配置为使用服务发现进行负载均衡：
- 用户服务: `lb://muying-mall-user`
- 商品服务: `lb://muying-mall-product`
- 订单服务: `lb://muying-mall-order`
- 支付服务: `lb://muying-mall-payment`
- 搜索服务: `lb://muying-mall-search`
- 物流服务: `lb://muying-mall-logistics`
- 评论服务: `lb://muying-mall-comment`
- 积分服务: `lb://muying-mall-points`
- 管理服务: `lb://muying-mall-admin`

## 配置文件说明

每个模块都包含以下配置文件：

### 1. bootstrap.yml
用于Nacos的早期启动配置，包含：
- 应用名称
- 环境配置
- Nacos服务器配置
- 命名空间和分组信息

### 2. application.yml
包含应用的具体配置，包括：
- 服务端口
- 数据库配置
- Redis配置
- Nacos详细配置
- 业务相关配置

## 启动说明

### 前置条件
1. Nacos服务器运行在 `localhost:8848`
2. Nacos认证用户名/密码: `nacos/nacos`
3. 创建命名空间 `dev`

### 推荐启动顺序
1. **先启动基础服务**:
   - Nacos (8848)
   - MySQL 
   - Redis

2. **然后启动微服务**:
   - muying-mall-gateway (8080)
   - muying-mall-user (8001)
   - muying-mall-product (8002)
   - muying-mall-order (8003)
   - muying-mall-payment (8004)
   - muying-mall-search (8005)
   - muying-mall-logistics (8006)
   - muying-mall-comment (8007)
   - muying-mall-points (8008)
   - muying-mall-admin (8009)

## 配置验证

启动完成后，可以通过以下方式验证配置：

1. **访问Nacos控制台**: http://localhost:8848/nacos
2. **检查服务列表**: 在服务管理 > 服务列表中查看所有服务是否注册成功
3. **检查配置列表**: 在配置管理 > 配置列表中查看配置是否正确加载
4. **测试网关路由**: 通过网关地址访问各个服务接口

## 故障排查

### 常见问题

1. **服务注册失败**
   - 检查Nacos服务器是否启动
   - 验证网络连接
   - 确认认证信息正确

2. **配置加载失败**
   - 检查命名空间是否存在
   - 确认配置文件名称正确
   - 验证分组设置

3. **网关路由失败**
   - 确认目标服务已注册到Nacos
   - 检查负载均衡配置
   - 验证路径匹配规则

### 日志查看
每个服务的日志级别已设置为DEBUG，可以查看详细的启动和运行日志来排查问题。

## 配置优势

1. **统一管理**: 所有配置集中在Nacos中管理
2. **动态刷新**: 支持配置热更新，无需重启服务
3. **环境隔离**: 通过命名空间实现不同环境的配置隔离
4. **服务发现**: 自动服务注册和发现，支持负载均衡
5. **高可用**: 支持Nacos集群部署提高可用性