package com.muyingmall.common.security.annotation;

import java.lang.annotation.*;

/**
 * 需要认证注解
 * 
 * <p>标记在方法或类上，表示访问该资源需要用户已认证（登录）。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RestController
 * @RequiresAuthentication
 * public class UserController {
 *     
 *     @GetMapping("/profile")
 *     public Result<UserVO> getProfile() {
 *         // 需要用户已登录才能访问
 *         return Result.success(userService.getCurrentUserProfile());
 *     }
 * }
 * 
 * // 或者在方法级别使用
 * @RestController
 * public class ProductController {
 *     
 *     @GetMapping("/list")
 *     public Result<List<ProductVO>> getProductList() {
 *         // 公开接口，无需认证
 *         return Result.success(productService.getProductList());
 *     }
 *     
 *     @PostMapping("/add")
 *     @RequiresAuthentication
 *     public Result<Void> addProduct(@RequestBody ProductDTO productDTO) {
 *         // 需要用户已登录才能添加商品
 *         productService.addProduct(productDTO);
 *         return Result.success();
 *     }
 * }
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>该注解需要配合拦截器或AOP切面使用</li>
 *   <li>方法级别的注解优先级高于类级别</li>
 *   <li>如果用户未认证，应返回401 Unauthorized状态码</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresAuthentication {
    
    /**
     * 注解描述信息
     * 
     * @return 描述信息，默认为空字符串
     */
    String value() default "";
    
    /**
     * 是否允许匿名访问
     * 
     * <p>当设置为true时，即使用户未认证也允许访问，但会尝试获取用户信息。
     * 这在某些需要区分登录和未登录用户的场景中很有用。</p>
     * 
     * @return true表示允许匿名访问，false表示必须认证
     */
    boolean allowAnonymous() default false;
    
    /**
     * 认证失败时的错误消息
     * 
     * @return 错误消息，默认为"用户未认证，请先登录"
     */
    String message() default "用户未认证，请先登录";
}