# RabbitMQ 快速开始指南

## 1. 环境准备

### 1.1 使用Docker启动RabbitMQ
```bash
# 拉取RabbitMQ镜像（包含管理界面）
docker pull rabbitmq:3-management

# 运行RabbitMQ容器
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin123 \
  -v rabbitmq_data:/var/lib/rabbitmq \
  rabbitmq:3-management
```

### 1.2 验证安装
- 管理界面: http://localhost:15672
- 默认账号: admin/admin123

## 2. 项目集成步骤

### Step 1: 添加依赖
在需要使用RabbitMQ的服务模块的pom.xml中添加：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### Step 2: 配置文件
在application.yml中添加RabbitMQ配置：
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123
    virtual-host: /muying
```

### Step 3: 创建配置类
```java
@Configuration
@EnableRabbit
public class RabbitMQConfig {
    
    // 定义交换机
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.exchange", true, false);
    }
    
    // 定义队列
    @Bean
    public Queue orderCreateQueue() {
        return new Queue("order.create.queue", true);
    }
    
    // 绑定队列到交换机
    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
            .bind(orderCreateQueue())
            .to(orderExchange())
            .with("order.create");
    }
    
    // 配置消息转换器
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```

### Step 4: 发送消息
```java
@Service
public class OrderService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void createOrder(Order order) {
        // 业务处理...
        
        // 发送消息
        OrderMessage message = new OrderMessage();
        message.setOrderId(order.getId());
        message.setUserId(order.getUserId());
        message.setAmount(order.getAmount());
        
        rabbitTemplate.convertAndSend(
            "order.exchange", 
            "order.create", 
            message
        );
    }
}
```

### Step 5: 接收消息
```java
@Component
public class OrderConsumer {
    
    @RabbitListener(queues = "order.create.queue")
    public void handleOrderCreate(OrderMessage message) {
        System.out.println("收到订单创建消息: " + message);
        // 处理消息
    }
}
```

## 3. 测试验证

### 3.1 发送测试消息
```java
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @GetMapping("/send")
    public String sendTestMessage() {
        Map<String, Object> message = new HashMap<>();
        message.put("id", UUID.randomUUID().toString());
        message.put("content", "测试消息");
        message.put("timestamp", System.currentTimeMillis());
        
        rabbitTemplate.convertAndSend(
            "order.exchange", 
            "order.create", 
            message
        );
        
        return "消息发送成功";
    }
}
```

### 3.2 查看管理界面
1. 访问 http://localhost:15672
2. 查看Queues标签页，确认消息数量
3. 查看Connections标签页，确认服务连接

## 4. 常用命令

### Docker命令
```bash
# 查看容器日志
docker logs rabbitmq

# 进入容器
docker exec -it rabbitmq bash

# 查看队列状态
docker exec rabbitmq rabbitmqctl list_queues

# 查看交换机
docker exec rabbitmq rabbitmqctl list_exchanges

# 查看绑定关系
docker exec rabbitmq rabbitmqctl list_bindings
```

### RabbitMQ CLI命令
```bash
# 创建虚拟主机
rabbitmqctl add_vhost /muying

# 创建用户
rabbitmqctl add_user myuser mypass

# 设置用户权限
rabbitmqctl set_permissions -p /muying myuser ".*" ".*" ".*"

# 查看用户列表
rabbitmqctl list_users

# 删除队列
rabbitmqctl delete_queue order.create.queue
```

## 5. 项目中的实际应用场景

### 5.1 订单服务 → 库存服务
```
订单创建 → 发送消息 → 库存服务接收 → 扣减库存
```

### 5.2 支付服务 → 订单服务
```
支付成功 → 发送消息 → 订单服务接收 → 更新订单状态
```

### 5.3 用户服务 → 积分服务
```
用户注册 → 发送消息 → 积分服务接收 → 赠送注册积分
```

## 6. 注意事项

1. **消息持久化**：确保重要消息设置为持久化
2. **消费确认**：使用手动确认模式处理重要业务
3. **错误处理**：实现死信队列处理失败消息
4. **监控告警**：定期检查队列堆积情况
5. **性能优化**：合理设置消费者数量和预取值

## 7. 故障排查

### 问题1: 连接失败
```
错误：Connection refused
解决：检查RabbitMQ服务是否启动，端口是否正确
```

### 问题2: 消息丢失
```
错误：消息发送后消费者未收到
解决：检查交换机、队列、绑定关系是否正确
```

### 问题3: 消息堆积
```
现象：队列中消息数量持续增长
解决：增加消费者数量或优化消费逻辑
```

## 8. 下一步

- 阅读[完整集成指南](./RabbitMQ集成指南.md)
- 查看[示例代码](../muying-mall-common/src/main/java/com/muyingmall/common/rabbitmq/)
- 参考[官方文档](https://www.rabbitmq.com/getstarted.html)