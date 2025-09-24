package com.muyingmall.common.security.annotation;

import java.lang.annotation.*;

/**
 * 需要角色注解
 * 
 * <p>标记在方法或类上，表示访问该资源需要用户具有指定的角色。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RestController
 * @RequiresRole("ADMIN")
 * public class AdminController {
 *     
 *     @GetMapping("/users")
 *     public Result<List<UserVO>> getAllUsers() {
 *         // 需要ADMIN角色才能访问
 *         return Result.success(userService.getAllUsers());
 *     }
 * }
 * 
 * // 多角色支持
 * @RestController
 * public class OrderController {
 *     
 *     @GetMapping("/list")
 *     @RequiresRole(value = {"ADMIN", "MANAGER"}, logical = Logical.OR)
 *     public Result<List<OrderVO>> getOrderList() {
 *         // 需要ADMIN或MANAGER角色才能访问
 *         return Result.success(orderService.getOrderList());
 *     }
 *     
 *     @PostMapping("/approve")
 *     @RequiresRole(value = {"ADMIN", "FINANCE"}, logical = Logical.AND)
 *     public Result<Void> approveOrder(@RequestParam Long orderId) {
 *         // 需要同时具有ADMIN和FINANCE角色才能访问
 *         orderService.approveOrder(orderId);
 *         return Result.success();
 *     }
 * }
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>该注解需要配合拦截器或AOP切面使用</li>
 *   <li>角色验证前会先检查用户是否已认证</li>
 *   <li>如果用户角色不匹配，应返回403 Forbidden状态码</li>
 *   <li>角色名称区分大小写</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {
    
    /**
     * 需要的角色名称
     * 
     * <p>可以指定单个角色或多个角色。</p>
     * 
     * @return 角色名称数组
     */
    String[] value();
    
    /**
     * 多角色的逻辑关系
     * 
     * <p>当指定多个角色时，定义角色之间的逻辑关系：</p>
     * <ul>
     *   <li>AND: 用户必须同时具有所有指定角色</li>
     *   <li>OR: 用户只需具有任意一个指定角色</li>
     * </ul>
     * 
     * @return 逻辑关系，默认为OR
     */
    Logical logical() default Logical.OR;
    
    /**
     * 角色验证失败时的错误消息
     * 
     * @return 错误消息，默认为"用户角色权限不足"
     */
    String message() default "用户角色权限不足";
    
    /**
     * 逻辑关系枚举
     */
    enum Logical {
        /**
         * 逻辑与：必须同时满足所有条件
         */
        AND,
        
        /**
         * 逻辑或：满足任意一个条件即可
         */
        OR
    }
}