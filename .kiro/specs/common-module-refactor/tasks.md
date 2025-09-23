# Common模块重构实施任务清单

## 实施计划概述

基于设计文档，将common模块重构分为4个主要阶段，每个阶段包含具体的编码任务。所有任务都专注于代码实现，确保可以由编程代理执行。

## 实施任务清单

### 阶段一：基础架构搭建

- [x] 1. 创建父POM模块结构


  - 修改现有muying-mall-common/pom.xml为父POM配置
  - 设置packaging为pom，添加modules声明
  - 配置统一的依赖管理和版本控制
  - _需求: 1.1, 1.3_
  - review:true

- [x] 2. 创建muying-mall-common-core子模块





  - 创建core模块的目录结构和pom.xml
  - 配置core模块的基础依赖
  - 建立标准的Maven目录结构
  - _需求: 2.1, 5.3_
  - review:true

- [x] 3. 创建其他子模块的基础结构





  - 创建redis、swagger、security、rabbitmq、web、all模块目录
  - 为每个子模块创建pom.xml配置文件
  - 建立各模块的Maven标准目录结构
  - _需求: 2.2, 2.3, 2.4, 2.5, 2.6_
  - review:true

### 阶段二：核心功能迁移

- [x] 4. 迁移异常处理框架到core模块






  - 将现有exception包下的所有异常类迁移到core模块
  - 创建统一的异常处理基类和业务异常类
  - 实现全局异常处理器
  - _需求: 4.1, 5.1_
  - review:true

- [-] 5. 迁移枚举和常量定义到core模块

  - 合并constants和constant包，统一为constant包
  - 将所有枚举类迁移到core模块的enums包
  - 将所有常量类迁移到core模块的constant包
  - _需求: 4.1, 5.2_
  - review:true

- [ ] 6. 迁移响应结果封装到core模块
  - 将ApiResponse、result包下的类迁移到core模块
  - 统一响应结果格式，实现Result和PageResult类
  - 创建响应结果构建工具类
  - _需求: 4.1, 5.1_
  - review:true

- [ ] 7. 迁移通用工具类到core模块
  - 将util和utils包下的通用工具类迁移到core模块
  - 清理重复的工具类，保留最优实现
  - 为工具类添加完整的JavaDoc文档
  - _需求: 4.2, 5.2_
  - review:true

### 阶段三：功能模块拆分

- [ ] 8. 实现Redis模块功能
  - 将Redis相关配置类迁移到redis模块
  - 实现Redis工具类和缓存服务
  - 创建分布式锁实现
  - 添加Redis自动配置类
  - _需求: 2.2, 4.3_
  - review:true

- [ ] 9. 实现Swagger模块功能
  - 创建Swagger配置类和属性类
  - 实现API文档自动配置
  - 添加Swagger相关注解增强
  - 配置接口分组和文档生成
  - _需求: 2.3, 4.3_
  - review:true

- [ ] 10. 实现Security模块功能
  - 将JWT相关工具类迁移到security模块
  - 实现密码加密和验证功能
  - 创建安全相关注解
  - 添加权限验证工具类
  - _需求: 2.4, 4.3_
  - review:true

- [ ] 11. 实现RabbitMQ模块功能
  - 将现有rabbitmq包下的类迁移到rabbitmq模块
  - 实现消息生产者和消费者模板
  - 创建RabbitMQ自动配置类
  - 添加消息队列工具类
  - _需求: 2.5, 4.3_
  - review:true

- [ ] 12. 实现Web模块功能
  - 将Web相关配置类迁移到web模块
  - 实现全局异常处理器
  - 创建请求响应拦截器
  - 添加跨域配置和Web工具类
  - _需求: 2.6, 4.3_
  - review:true

### 阶段四：兼容性保障和优化

- [ ] 13. 创建聚合模块(common-all)
  - 实现common-all模块，依赖所有功能子模块
  - 配置自动装配，确保所有功能可用
  - 创建向后兼容的包结构映射
  - _需求: 6.1, 6.2_
  - review:true

- [ ] 14. 更新现有微服务的依赖配置
  - 修改各微服务pom.xml中的common模块依赖
  - 验证现有代码的导入语句是否正常工作
  - 测试各微服务的启动和基本功能
  - _需求: 6.3, 6.4_
  - review:true

- [ ] 15. 编写单元测试
  - 为core模块的所有工具类编写单元测试
  - 为各功能模块的关键类编写单元测试
  - 确保测试覆盖率达到80%以上
  - _需求: 4.5_
  - review:true

- [ ] 16. 创建集成测试
  - 编写模块间依赖关系的集成测试
  - 创建Spring Boot自动配置的集成测试
  - 验证各模块功能的端到端测试
  - _需求: 4.5_
  - review:true

- [ ] 17. 性能和兼容性验证
  - 运行现有微服务的完整测试套件
  - 进行性能基准测试，确保无明显性能下降
  - 验证所有现有业务流程正常工作
  - _需求: 6.4, 4.4_
  - review:true

- [ ] 18. 文档和迁移指南
  - 编写各模块的使用文档和API说明
  - 创建从旧结构到新结构的迁移指南
  - 提供最佳实践和配置示例
  - _需求: 7.4_
  - review:false

## 任务执行说明

### 依赖关系
- 任务1-3必须按顺序执行，建立基础架构
- 任务4-7可以并行执行，但都依赖任务2完成
- 任务8-12可以并行执行，但都依赖任务4-7完成
- 任务13-17必须在前面所有任务完成后执行
- 任务18可以在任务17完成后执行

### 验证标准
- 每个任务完成后必须能够成功编译
- 相关的单元测试必须通过
- 不能破坏现有功能的正常运行
- 代码必须符合项目的编码规范

### 回滚策略
- 每个阶段完成后创建代码快照
- 如果出现问题，可以回滚到上一个稳定状态
- 保留原始代码备份，直到重构完全验证通过
## 任务
执行进度更新

### 任务4执行记录

**[2025-09-23 18:32]**
- 步骤: 4. 迁移异常处理框架到core模块 (初步完成，需要交互式审查: review:true)
- 修改内容: 
  - 创建了BusinessException基类 (muying-mall-common-core/src/main/java/com/muyingmall/common/core/exception/BusinessException.java)
  - 创建了具体异常类: ValidationException, AuthenticationException, AuthorizationException, ResourceNotFoundException
  - 迁移并改进了Result类到core模块 (muying-mall-common-core/src/main/java/com/muyingmall/common/core/result/Result.java)
  - 创建了ErrorCode枚举类 (muying-mall-common-core/src/main/java/com/muyingmall/common/core/enums/ErrorCode.java)
  - 迁移并大幅改进了GlobalExceptionHandler (muying-mall-common-core/src/main/java/com/muyingmall/common/core/exception/GlobalExceptionHandler.java)
  - 创建了ExceptionUtils工具类 (muying-mall-common-core/src/main/java/com/muyingmall/common/core/exception/ExceptionUtils.java)
- 变更摘要: 成功将异常处理框架迁移到core模块，并进行了功能增强，包括更完善的异常类型、错误码枚举、异常工具类等
- 原因: 执行计划步骤4 - 迁移异常处理框架到core模块
- 障碍: 无
- 状态: 交互式审查已结束
- 用户确认状态: 成功 - 用户准备进入下一阶段
- 交互式审查脚本退出信息: 用户通过 'next' 关键词结束审查
**
[2025-09-23 18:55]**
- 步骤: 5. 迁移枚举和常量定义到core模块 (初步完成，需要交互式审查: review:true)
- 修改内容: 
  - 迁移了所有枚举类到core模块的enums包：MessageType, OrderStatus, PaymentStatus, PointsOperationType, RefundStatus
  - 改进了RefundStatus枚举，增加了完整的状态定义和业务方法
  - 合并并统一了常量定义到core模块的constant包：CommonConstants, CacheConstants, ApiErrorCodes, RabbitMQConstants
  - 统一了constants和constant包为constant包，避免了包名冲突
  - 为所有常量类添加了私有构造函数防止实例化
  - 更新了package-info.java文档
- 变更摘要: 成功将所有枚举和常量定义迁移到core模块，统一了包结构，增强了代码规范性
- 原因: 执行计划步骤5 - 迁移枚举和常量定义到core模块
- 障碍: 无
- 状态: 等待后续处理 (交互式审查)