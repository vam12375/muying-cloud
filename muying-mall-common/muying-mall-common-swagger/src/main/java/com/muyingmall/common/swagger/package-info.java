/**
 * 母婴商城公共模块 - Swagger API文档模块
 * 
 * <p>本模块提供了完整的API文档生成功能，基于SpringDoc OpenAPI实现。</p>
 * 
 * <h2>主要功能</h2>
 * <ul>
 *   <li>API文档自动生成</li>
 *   <li>接口分组管理</li>
 *   <li>自定义注解增强</li>
 *   <li>配置属性外部化</li>
 *   <li>条件化自动配置</li>
 * </ul>
 * 
 * <h2>使用方式</h2>
 * 
 * <h3>1. 添加依赖</h3>
 * <pre>{@code
 * <dependency>
 *     <groupId>com.muyingmall</groupId>
 *     <artifactId>muying-mall-common-swagger</artifactId>
 * </dependency>
 * }</pre>
 * 
 * <h3>2. 配置文件</h3>
 * <pre>{@code
 * muying:
 *   swagger:
 *     enabled: true
 *     title: "母婴商城API"
 *     version: "1.0.0"
 *     description: "母婴商城微服务API文档"
 *     contact:
 *       name: "开发团队"
 *       email: "dev@muyingmall.com"
 *     server:
 *       url: "http://localhost:8080"
 *       description: "开发环境"
 *     group:
 *       enabled: true
 *       base-package: "com.muyingmall"
 * }</pre>
 * 
 * <h3>3. 使用注解</h3>
 * <pre>{@code
 * @RestController
 * @ApiGroup("用户管理")
 * @ApiVersion("1.0")
 * public class UserController {
 *     
 *     @GetMapping("/users/{id}")
 *     @ApiResponseExample(code = 200, description = "成功", example = "{\"code\":200,\"data\":{\"id\":1,\"name\":\"张三\"}}")
 *     public Result<User> getUser(@PathVariable Long id) {
 *         // 实现逻辑
 *     }
 *     
 *     @PostMapping("/internal/users")
 *     @IgnoreSwagger("内部接口")
 *     public Result<Void> internalMethod() {
 *         // 内部接口，不会出现在API文档中
 *     }
 * }
 * }</pre>
 * 
 * <h2>包结构</h2>
 * <ul>
 *   <li>{@link com.muyingmall.common.swagger.config} - 配置类</li>
 *   <li>{@link com.muyingmall.common.swagger.properties} - 配置属性</li>
 *   <li>{@link com.muyingmall.common.swagger.annotation} - 自定义注解</li>
 * </ul>
 * 
 * <h2>访问地址</h2>
 * <ul>
 *   <li>Swagger UI: http://localhost:8080/swagger-ui.html</li>
 *   <li>OpenAPI JSON: http://localhost:8080/v3/api-docs</li>
 * </ul>
 * 
 * @author 青柠檬
 * @since 2025-09-23
 * @version 1.0.0
 */
package com.muyingmall.common.swagger;