# 母婴商城微服务架构 - RabbitMQ 集成指南

## 1. 概述

### 1.1 引入背景
在母婴商城微服务架构中，各个服务之间需要进行异步通信和解耦。RabbitMQ作为一个可靠的消息中间件，可以帮助我们实现：
- 服务间异步通信
- 削峰填谷
- 系统解耦
- 消息持久化和可靠传递

### 1.2 应用场景
- **订单处理流程**：订单创建后异步通知库存、积分、物流等服务
- **支付通知**：支付成功后通知订单服务更新状态
- **库存更新**：商品库存变动时通知搜索服务更新索引
- **评论审核**：评论提交后异步进行审核处理
- **积分变动**：用户积分变动时异步发送通知
- **物流跟踪**：物流状态更新时通知相关服务

## 2. 架构设计

### 2.1 消息队列拓扑结构
```
┌─────────────────────────────────────────────────────────────┐
│                         RabbitMQ Cluster                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                   Virtual Host: /muying              │    │
│  ├─────────────────────────────────────────────────────┤    │
│  │                                                      │    │
│  │  Exchanges:                                          │    │
│  │  ┌────────────────┐  ┌────────────────┐            │    │
│  │  │ order.exchange │  │ payment.exchange│            │    │
│  │  └────────────────┘  └────────────────┘            │    │
│  │  ┌────────────────┐  ┌────────────────┐            │    │
│  │  │product.exchange│  │ user.exchange   │            │    │
│  │  └────────────────┘  └────────────────┘            │    │
│  │                                                      │    │
│  │  Queues:                                            │    │
│  │  ┌──────────────────────────────────────┐          │    │
│  │  │ order.create.queue                    │          │    │
│  │  │ order.cancel.queue                    │          │    │
│  │  │ payment.success.queue                 │          │    │
│  │  │ payment.refund.queue                  │          │    │
│  │  │ product.stock.update.queue            │          │    │
│  │  │ user.points.update.queue              │          │    │
│  │  │ logistics.status.update.queue         │          │    │
│  │  └──────────────────────────────────────┘          │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Exchange 设计

| Exchange名称 | 类型 | 持久化 | 说明 |
|-------------|------|--------|------|
| order.exchange | topic | 是 | 订单相关消息交换机 |
| payment.exchange | topic | 是 | 支付相关消息交换机 |
| product.exchange | topic | 是 | 商品相关消息交换机 |
| user.exchange | topic | 是 | 用户相关消息交换机 |
| logistics.exchange | topic | 是 | 物流相关消息交换机 |
| comment.exchange | topic | 是 | 评论相关消息交换机 |
| points.exchange | topic | 是 | 积分相关消息交换机 |
| delay.exchange | x-delayed-message | 是 | 延迟消息交换机 |
| dlx.exchange | direct | 是 | 死信交换机 |

### 2.3 Queue 设计

| Queue名称 | 绑定Exchange | Routing Key | 消费者服务 | 说明 |
|----------|-------------|-------------|-----------|------|
| order.create.queue | order.exchange | order.create | 库存/积分/物流服务 | 订单创建通知 |
| order.cancel.queue | order.exchange | order.cancel | 库存/积分服务 | 订单取消通知 |
| order.timeout.queue | delay.exchange | order.timeout | 订单服务 | 订单超时处理 |
| payment.success.queue | payment.exchange | payment.success | 订单/积分服务 | 支付成功通知 |
| payment.refund.queue | payment.exchange | payment.refund | 订单/积分服务 | 退款通知 |
| product.stock.update.queue | product.exchange | product.stock.* | 搜索服务 | 库存更新通知 |
| user.register.queue | user.exchange | user.register | 积分/通知服务 | 用户注册通知 |
| user.points.update.queue | points.exchange | points.update | 用户服务 | 积分更新通知 |
| logistics.status.queue | logistics.exchange | logistics.status.* | 订单服务 | 物流状态更新 |
| comment.audit.queue | comment.exchange | comment.audit | 评论服务 | 评论审核队列 |

### 2.4 消息格式规范

```json
{
  "messageId": "UUID",
  "timestamp": "2024-01-01T10:00:00.000Z",
  "source": "服务名称",
  "eventType": "事件类型",
  "version": "1.0",
  "data": {
    // 业务数据
  },
  "metadata": {
    "userId": "操作用户ID",
    "traceId": "链路追踪ID",
    "retryCount": 0
  }
}
```

## 3. 技术实现

### 3.1 依赖配置

#### 父POM依赖管理
```xml
<!-- Spring Boot AMQP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- Jackson for JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson.version}</version>
</dependency>
```

#### 服务模块依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 3.2 配置文件

#### application.yml
```yaml
spring:
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USERNAME:admin}
    password: ${RABBITMQ_PASSWORD:admin123}
    virtual-host: /muying
    # 连接池配置
    connection-timeout: 15000
    cache:
      connection:
        size: 5
        mode: channel
      channel:
        size: 50
        checkout-timeout: 10000
    # 消息确认机制
    publisher-confirm-type: correlated
    publisher-returns: true
    template:
      mandatory: true
      retry:
        enabled: true
        initial-interval: 1000
        max-attempts: 3
        max-interval: 10000
        multiplier: 2
    # 消费者配置
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 5
        max-concurrency: 10
        prefetch: 1
        retry:
          enabled: true
          initial-interval: 1000
          max-attempts: 3
          max-interval: 10000
          multiplier: 2
```

### 3.3 核心配置类

见 `muying-mall-common/src/main/java/com/muyingmall/common/rabbitmq/` 目录

## 4. 使用示例

### 4.1 生产者示例

```java
@Service
@Slf4j
public class OrderMessageProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送订单创建消息
     */
    public void sendOrderCreateMessage(OrderCreateMessage message) {
        try {
            // 设置消息ID
            message.setMessageId(UUID.randomUUID().toString());
            message.setTimestamp(new Date());
            
            // 发送消息
            rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.create",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    msg.getMessageProperties().setMessageId(message.getMessageId());
                    return msg;
                }
            );
            
            log.info("订单创建消息发送成功: {}", message.getMessageId());
        } catch (Exception e) {
            log.error("订单创建消息发送失败", e);
            // 可以将失败的消息保存到数据库，后续重试
        }
    }
}
```

### 4.2 消费者示例

```java
@Component
@Slf4j
public class OrderMessageConsumer {
    
    @RabbitListener(queues = "order.create.queue")
    public void handleOrderCreate(OrderCreateMessage message, Channel channel, 
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            log.info("接收到订单创建消息: {}", message);
            
            // 处理业务逻辑
            processOrderCreate(message);
            
            // 手动确认消息
            channel.basicAck(tag, false);
            
        } catch (BusinessException e) {
            // 业务异常，不重试，直接确认
            try {
                channel.basicAck(tag, false);
                log.error("处理订单创建消息业务异常: {}", e.getMessage());
            } catch (IOException ioException) {
                log.error("消息确认失败", ioException);
            }
        } catch (Exception e) {
            // 系统异常，拒绝消息，进行重试
            try {
                channel.basicNack(tag, false, true);
                log.error("处理订单创建消息失败，将重试", e);
            } catch (IOException ioException) {
                log.error("消息拒绝失败", ioException);
            }
        }
    }
}
```

### 4.3 延迟消息示例

```java
@Service
public class DelayMessageService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送延迟消息（如订单超时取消）
     */
    public void sendDelayMessage(String orderId, long delayMillis) {
        OrderTimeoutMessage message = new OrderTimeoutMessage();
        message.setOrderId(orderId);
        message.setMessageId(UUID.randomUUID().toString());
        
        rabbitTemplate.convertAndSend(
            "delay.exchange",
            "order.timeout",
            message,
            msg -> {
                msg.getMessageProperties().setDelay((int) delayMillis);
                return msg;
            }
        );
    }
}
```

## 5. 监控与运维

### 5.1 RabbitMQ Management UI

- 访问地址：http://localhost:15672
- 默认账号：admin/admin123
- 功能：
  - 查看队列状态
  - 监控消息堆积
  - 管理Exchange和Queue
  - 查看连接和通道

### 5.2 关键指标监控

| 指标 | 说明 | 告警阈值 |
|-----|------|---------|
| 队列消息堆积数 | 未消费消息数量 | > 1000 |
| 消费者数量 | 每个队列的消费者数 | < 1 |
| 连接数 | 客户端连接数 | > 100 |
| 内存使用率 | RabbitMQ内存占用 | > 80% |
| 磁盘使用率 | 消息持久化磁盘占用 | > 80% |
| 消息发送速率 | 每秒发送消息数 | 根据业务设定 |
| 消息消费速率 | 每秒消费消息数 | 根据业务设定 |

### 5.3 日志配置

```yaml
logging:
  level:
    org.springframework.amqp: DEBUG
    com.rabbitmq: INFO
    com.muyingmall.common.rabbitmq: DEBUG
```

## 6. 最佳实践

### 6.1 消息幂等性
- 每个消息包含唯一messageId
- 消费端维护消息处理记录表
- 处理前检查是否已处理

### 6.2 错误处理
- 区分业务异常和系统异常
- 业务异常不重试，记录日志
- 系统异常自动重试，超过次数进入死信队列

### 6.3 性能优化
- 合理设置prefetch值
- 使用批量确认提升性能
- 持久化与非持久化消息的权衡

### 6.4 安全考虑
- 使用独立的虚拟主机
- 设置合适的用户权限
- 敏感数据加密传输
- 启用SSL/TLS连接

## 7. 故障处理

### 7.1 常见问题

| 问题 | 原因 | 解决方案 |
|-----|------|---------|
| 消息丢失 | 未开启持久化或确认机制 | 开启消息和队列持久化，使用确认机制 |
| 消息重复 | 网络问题或消费者异常 | 实现幂等性处理 |
| 消息堆积 | 消费速度慢或消费者宕机 | 增加消费者或优化消费逻辑 |
| 连接断开 | 网络问题或超时 | 配置重连机制和心跳检测 |

### 7.2 应急预案
1. **消息堆积处理**
   - 临时增加消费者实例
   - 将部分消息转移到备用队列
   - 必要时清理过期消息

2. **服务降级**
   - 关闭非核心业务的消息生产
   - 降低消息发送频率
   - 启用备用处理方案

## 8. 测试指南

### 8.1 单元测试
```java
@SpringBootTest
@AutoConfigureMockMvc
public class RabbitMQTest {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Test
    public void testSendMessage() {
        // 测试消息发送
    }
}
```

### 8.2 集成测试
- 使用Testcontainers启动RabbitMQ容器
- 验证消息的完整流程
- 测试异常场景的处理

## 9. 版本更新记录

| 版本 | 日期 | 更新内容 |
|------|------|---------|
| 1.0.0 | 2024-01-15 | 初始版本，基础RabbitMQ集成 |

## 10. 参考资料

- [RabbitMQ官方文档](https://www.rabbitmq.com/documentation.html)
- [Spring AMQP Reference](https://docs.spring.io/spring-amqp/reference/html/)
- [AMQP 0-9-1协议规范](https://www.rabbitmq.com/amqp-0-9-1-reference.html)