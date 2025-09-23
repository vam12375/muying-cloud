# 母婴商城公共模块 (muying-mall-common)

## 模块架构

本项目采用多模块架构，将原有的单体common模块拆分为以下子模块：

```
muying-mall-common (父模块)
├── muying-mall-common-core        # 核心基础模块
├── muying-mall-common-redis       # Redis缓存模块  
├── muying-mall-common-swagger     # API文档模块
├── muying-mall-common-security    # 安全认证模块
├── muying-mall-common-rabbitmq    # 消息队列模块
├── muying-mall-common-web         # Web相关模块
└── muying-mall-common-all         # 聚合模块(向后兼容)
```

## 模块说明

### muying-mall-common-core
- **职责**: 提供基础功能和通用组件
- **包含**: 异常处理、枚举定义、常量定义、通用工具类、响应结果封装

### muying-mall-common-redis
- **职责**: 提供Redis缓存相关功能
- **包含**: Redis配置、缓存工具类、分布式锁、缓存注解

### muying-mall-common-swagger
- **职责**: 提供API文档生成功能
- **包含**: Swagger配置、API文档注解增强、接口分组配置

### muying-mall-common-security
- **职责**: 提供安全认证相关功能
- **包含**: JWT工具类、密码加密、安全注解、权限验证

### muying-mall-common-rabbitmq
- **职责**: 提供RabbitMQ消息队列功能
- **包含**: RabbitMQ配置、消息生产者、消息消费者、消息模板

### muying-mall-common-web
- **职责**: 提供Web相关功能
- **包含**: 全局异常处理、请求响应拦截器、跨域配置、Web工具类

### muying-mall-common-all
- **职责**: 聚合模块，提供向后兼容性
- **包含**: 依赖所有子模块，提供统一入口

## 依赖关系

- `core` 模块是最基础的模块，不依赖其他common子模块
- 其他功能模块都依赖 `core` 模块
- `all` 模块聚合所有功能模块，用于向后兼容

## 构建说明

```bash
# 构建所有模块
mvn clean compile -f muying-mall-common/pom.xml

# 构建特定模块
mvn clean compile -f muying-mall-common/muying-mall-common-core/pom.xml
```

## 使用方式

### 按需引入特定功能模块
```xml
<!-- 只需要Redis功能 -->
<dependency>
    <groupId>com.muyingmall</groupId>
    <artifactId>muying-mall-common-redis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 引入所有功能（向后兼容）
```xml
<!-- 获得所有功能 -->
<dependency>
    <groupId>com.muyingmall</groupId>
    <artifactId>muying-mall-common-all</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 开发状态

- ✅ 基础架构搭建完成
- ✅ 所有子模块目录结构创建完成
- ✅ Maven标准目录结构建立完成
- ✅ 构建验证通过
- ⏳ 待进行：核心功能迁移（下一阶段任务）