package com.muyingmall.common.security.annotation;

import java.lang.annotation.*;

/**
 * 需要权限注解
 * 
 * <p>标记在方法或类上，表示访问该资源需要用户具有指定的权限。</p>
 * 
 * <p>权限格式建议使用"资源:操作"的格式，例如：</p>
 * <ul>
 *   <li>user:read - 用户读取权限</li>
 *   <li>user:write - 用户写入权限</li>
 *   <li>product:create - 商品创建权限</li>
 *   <li>order:delete - 订单删除权限</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/users")
 * public class UserController {
 *     
 *     @GetMapping("/{id}")
 *     @RequiresPermission("user:read")
 *     public Result<UserVO> getUser(@PathVariable Long id) {
 *         // 需要user:read权限才能访问
 *         return Result.success(userService.getUser(id));
 *     }
 *     
 *     @PostMapping
 *     @RequiresPermission("user:create")
 *     public Result<Void> createUser(@RequestBody UserDTO userDTO) {
 *         // 需要user:create权限才能访问
 *         userService.createUser(userDTO);
 *         return Result.success();
 *     }
 *     
 *     @DeleteMapping("/{id}")
 *     @RequiresPermission(value = {"user:delete", "admin:all"}, logical = Logical.OR)
 *     public Result<Void> deleteUser(@PathVariable Long id) {
 *         // 需要user:delete权限或admin:all权限才能访问
 *         userService.deleteUser(id);
 *         return Result.success();
 *     }
 * }
 * 
 * // 类级别权限
 * @RestController
 * @RequestMapping("/api/admin")
 * @RequiresPermission("admin:access")
 * public class AdminController {
 *     
 *     @GetMapping("/dashboard")
 *     public Result<DashboardVO> getDashboard() {
 *         // 继承类级别的admin:access权限要求
 *         return Result.success(adminService.getDashboard());
 *     }
 *     
 *     @PostMapping("/system/config")
 *     @RequiresPermission("system:config")
 *     public Result<Void> updateSystemConfig(@RequestBody ConfigDTO configDTO) {
 *         // 需要同时具有admin:access和system:config权限
 *         adminService.updateSystemConfig(configDTO);
 *         return Result.success();
 *     }
 * }
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>该注解需要配合拦截器或AOP切面使用</li>
 *   <li>权限验证前会先检查用户是否已认证</li>
 *   <li>如果用户权限不足，应返回403 Forbidden状态码</li>
 *   <li>权限名称区分大小写</li>
 *   <li>方法级别的权限要求会与类级别的权限要求叠加</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    
    /**
     * 需要的权限名称
     * 
     * <p>可以指定单个权限或多个权限。建议使用"资源:操作"的格式。</p>
     * 
     * @return 权限名称数组
     */
    String[] value();
    
    /**
     * 多权限的逻辑关系
     * 
     * <p>当指定多个权限时，定义权限之间的逻辑关系：</p>
     * <ul>
     *   <li>AND: 用户必须同时具有所有指定权限</li>
     *   <li>OR: 用户只需具有任意一个指定权限</li>
     * </ul>
     * 
     * @return 逻辑关系，默认为AND
     */
    Logical logical() default Logical.AND;
    
    /**
     * 权限验证失败时的错误消息
     * 
     * @return 错误消息，默认为"用户权限不足"
     */
    String message() default "用户权限不足";
    
    /**
     * 是否检查类级别权限
     * 
     * <p>当设置为true时，会同时检查类级别和方法级别的权限要求。
     * 当设置为false时，只检查方法级别的权限要求。</p>
     * 
     * @return true表示检查类级别权限，false表示只检查方法级别权限
     */
    boolean checkClass() default true;
    
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