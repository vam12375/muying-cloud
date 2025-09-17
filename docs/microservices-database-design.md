# 母婴商城微服务数据库架构设计

## 数据库拆分方案

基于原始单体数据库`muying_mall.sql`的表结构分析，按照业务领域拆分为以下微服务数据库：

### 1. 用户服务数据库 (muying_user_service)
**业务职责**: 用户账户管理、会员体系、用户资料
**包含表**:
- `user` - 用户基本信息表
- `user_account` - 用户账户表  
- `user_address` - 用户地址表
- `member_level` - 会员等级表
- `account_transaction` - 账户交易记录表

### 2. 商品服务数据库 (muying_product_service)
**业务职责**: 商品信息管理、分类品牌管理、库存管理
**包含表**:
- `product` - 商品表
- `category` - 商品分类表
- `brand` - 品牌表
- `product_image` - 商品图片表
- `product_param` - 商品参数表
- `product_specs` - 商品规格表
- `spec_value` - 规格值表

### 3. 订单服务数据库 (muying_order_service)
**业务职责**: 订单管理、购物车管理、订单状态跟踪
**包含表**:
- `order` - 订单表
- `order_product` - 订单商品表
- `order_state_log` - 订单状态日志表
- `cart` - 购物车表

### 4. 支付服务数据库 (muying_payment_service)
**业务职责**: 支付处理、退款管理、支付记录
**包含表**:
- `payment` - 支付表
- `payment_log` - 支付日志表
- `payment_state_log` - 支付状态日志表
- `refund` - 退款表
- `refund_log` - 退款日志表

### 5. 物流服务数据库 (muying_logistics_service)
**业务职责**: 物流信息管理、快递跟踪
**包含表**:
- `logistics` - 物流表
- `logistics_company` - 物流公司表
- `logistics_trace` - 物流轨迹表
- `logistics_track` - 物流跟踪表

### 6. 营销服务数据库 (muying_marketing_service)
**业务职责**: 优惠券管理、积分系统、营销活动
**包含表**:
- `coupon` - 优惠券表
- `coupon_batch` - 优惠券批次表
- `coupon_rule` - 优惠券规则表
- `user_coupon` - 用户优惠券表
- `user_points` - 用户积分表
- `points_history` - 积分历史表
- `points_rule` - 积分规则表
- `points_product` - 积分商品表
- `points_exchange` - 积分兑换表

### 7. 评价服务数据库 (muying_review_service)
**业务职责**: 商品评价管理、评价回复、评价统计
**包含表**:
- `comment` - 评价表
- `comment_reply` - 评价回复表
- `comment_tag` - 评价标签表
- `comment_tag_relation` - 评价标签关系表
- `comment_template` - 评价模板表
- `comment_reward_config` - 评价奖励配置表

### 8. 系统管理服务数据库 (muying_admin_service)
**业务职责**: 管理员管理、操作日志、系统监控
**包含表**:
- `admin_login_records` - 管理员登录记录表
- `admin_online_status` - 管理员在线状态表
- `admin_operation_logs` - 管理员操作日志表

### 9. 用户行为服务数据库 (muying_behavior_service)
**业务职责**: 用户行为分析、收藏管理、搜索统计
**包含表**:
- `favorite` - 收藏表
- `user_message` - 用户消息表
- `search_statistics` - 搜索统计表

## 跨服务数据一致性策略

### 1. 事件驱动架构
- 使用消息队列(RabbitMQ/Kafka)实现最终一致性
- 重要业务流程采用Saga模式

### 2. 数据冗余策略  
- 在订单服务中冗余用户基本信息和商品基本信息
- 在评价服务中冗余商品基本信息
- 使用事件同步保持冗余数据一致性

### 3. 分布式事务处理
- 核心业务流程(下单、支付)使用分布式事务
- 非核心业务采用最终一致性

## 数据库连接配置

每个微服务使用独立的数据库连接配置，支持读写分离和连接池管理。