# muying-mall-common-core

母婴商城核心公共模块，提供基础功能和通用组件。

## 功能特性

- **异常处理框架**: 统一的异常处理和响应封装
- **枚举定义**: 系统中使用的各种枚举类型
- **常量定义**: 系统常量和配置常量
- **通用工具类**: 各种实用工具类
- **基础注解**: 系统通用注解定义
- **响应结果封装**: 统一的API响应格式
- **分页组件**: 分页查询支持
- **基础领域对象**: 通用的实体基类

## 包结构

```
com.muyingmall.common.core
├── annotation/          # 通用注解
├── constant/           # 系统常量
├── enums/              # 枚举定义
├── exception/          # 异常处理
├── result/             # 响应结果封装
├── utils/              # 通用工具类
└── domain/             # 基础领域对象
```

## 依赖说明

本模块是其他common子模块的基础依赖，包含：

- Spring Boot Starter
- Spring Boot Validation
- Spring Boot AOP
- FastJSON2
- Apache Commons Lang3
- Commons IO
- Jakarta Servlet API
- Jakarta Annotation API

## 使用方式

在需要使用核心功能的模块中添加依赖：

```xml
<dependency>
    <groupId>com.muyingmall</groupId>
    <artifactId>muying-mall-common-core</artifactId>
    <version>${project.version}</version>
</dependency>
```

## 版本信息

- 版本: 1.0.0
- Java版本: 21
- Spring Boot版本: 3.5.5