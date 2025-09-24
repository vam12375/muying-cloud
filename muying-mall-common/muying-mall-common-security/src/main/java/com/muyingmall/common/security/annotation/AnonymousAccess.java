package com.muyingmall.common.security.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问注解
 * 
 * <p>标记在方法或类上，表示该资源允许匿名访问，无需认证。</p>
 * 
 * <p>该注解通常用于：</p>
 * <ul>
 *   <li>公开API接口</li>
 *   <li>登录、注册等认证相关接口</li>
 *   <li>静态资源访问</li>
 *   <li>健康检查接口</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/auth")
 * @AnonymousAccess
 * public class AuthController {
 *     
 *     @PostMapping("/login")
 *     public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
 *         // 登录接口允许匿名访问
 *         return Result.success(authService.login(loginDTO));
 *     }
 *     
 *     @PostMapping("/register")
 *     public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
 *         // 注册接口允许匿名访问
 *         authService.register(registerDTO);
 *         return Result.success();
 *     }
 * }
 * 
 * // 在需要认证的Controller中标记特定方法为匿名访问
 * @RestController
 * @RequestMapping("/api/products")
 * @RequiresAuthentication
 * public class ProductController {
 *     
 *     @GetMapping("/public/list")
 *     @AnonymousAccess
 *     public Result<List<ProductVO>> getPublicProductList() {
 *         // 公开商品列表，允许匿名访问
 *         return Result.success(productService.getPublicProductList());
 *     }
 *     
 *     @GetMapping("/my/favorites")
 *     public Result<List<ProductVO>> getMyFavorites() {
 *         // 个人收藏，需要认证
 *         return Result.success(productService.getMyFavorites());
 *     }
 * }
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>该注解优先级高于认证和权限注解</li>
 *   <li>方法级别的注解优先级高于类级别</li>
 *   <li>即使标记为匿名访问，仍可以尝试获取用户信息（如果用户已登录）</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousAccess {
    
    /**
     * 注解描述信息
     * 
     * @return 描述信息，默认为空字符串
     */
    String value() default "";
    
    /**
     * 是否记录访问日志
     * 
     * <p>对于匿名访问的接口，可以选择是否记录访问日志。</p>
     * 
     * @return true表示记录访问日志，false表示不记录
     */
    boolean logAccess() default true;
}