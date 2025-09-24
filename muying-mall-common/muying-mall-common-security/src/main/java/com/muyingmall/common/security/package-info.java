/**
 * 母婴商城安全认证模块
 * 
 * <p>提供完整的安全认证和权限管理功能，包括JWT令牌管理、密码加密、权限验证等。</p>
 * 
 * <h2>主要功能</h2>
 * <ul>
 *   <li><strong>JWT令牌管理</strong> - 支持令牌生成、验证、刷新和权限检查</li>
 *   <li><strong>密码加密服务</strong> - 提供BCrypt和SHA-256加密算法</li>
 *   <li><strong>安全注解</strong> - 支持方法级和类级的权限控制</li>
 *   <li><strong>权限验证工具</strong> - 便捷的认证和权限检查方法</li>
 *   <li><strong>自动配置</strong> - Spring Boot自动配置支持</li>
 * </ul>
 * 
 * <h2>核心组件</h2>
 * <ul>
 *   <li>{@link com.muyingmall.common.security.jwt.JwtUtils} - JWT工具类</li>
 *   <li>{@link com.muyingmall.common.security.crypto.PasswordEncoder} - 密码加密服务</li>
 *   <li>{@link com.muyingmall.common.security.utils.AuthenticationUtils} - 认证工具类</li>
 *   <li>{@link com.muyingmall.common.security.annotation.RequiresAuthentication} - 认证注解</li>
 *   <li>{@link com.muyingmall.common.security.annotation.RequiresPermission} - 权限注解</li>
 *   <li>{@link com.muyingmall.common.security.annotation.RequiresRole} - 角色注解</li>
 * </ul>
 * 
 * <h2>快速开始</h2>
 * 
 * <h3>1. 添加依赖</h3>
 * <pre>{@code
 * <dependency>
 *     <groupId>com.muyingmall</groupId>
 *     <artifactId>muying-mall-common-security</artifactId>
 *     <version>1.0.0</version>
 * </dependency>
 * }</pre>
 * 
 * <h3>2. 配置属性</h3>
 * <pre>{@code
 * # application.yml
 * muying:
 *   security:
 *     enabled: true
 *     jwt:
 *       secret: "your-secret-key-here"
 *       expiration: 86400
 *     cors:
 *       enabled: true
 *       allowed-origins: "*"
 * }</pre>
 * 
 * <h3>3. 使用JWT工具类</h3>
 * <pre>{@code
 * @Autowired
 * private JwtUtils jwtUtils;
 * 
 * // 生成令牌
 * String token = jwtUtils.generateToken(userId, username, role);
 * 
 * // 验证令牌
 * boolean isValid = jwtUtils.validateToken(token);
 * 
 * // 获取用户信息
 * Integer userId = jwtUtils.getUserIdFromToken(token);
 * }</pre>
 * 
 * <h3>4. 使用安全注解</h3>
 * <pre>{@code
 * @RestController
 * @RequiresAuthentication
 * public class UserController {
 *     
 *     @GetMapping("/profile")
 *     public Result<UserVO> getProfile() {
 *         // 需要用户已认证
 *         return Result.success(userService.getCurrentUserProfile());
 *     }
 *     
 *     @PostMapping("/admin/users")
 *     @RequiresRole("ADMIN")
 *     public Result<Void> createUser(@RequestBody UserDTO userDTO) {
 *         // 需要ADMIN角色
 *         userService.createUser(userDTO);
 *         return Result.success();
 *     }
 *     
 *     @DeleteMapping("/users/{id}")
 *     @RequiresPermission("user:delete")
 *     public Result<Void> deleteUser(@PathVariable Long id) {
 *         // 需要user:delete权限
 *         userService.deleteUser(id);
 *         return Result.success();
 *     }
 * }
 * }</pre>
 * 
 * <h3>5. 使用密码加密</h3>
 * <pre>{@code
 * @Autowired
 * private PasswordEncoder passwordEncoder;
 * 
 * // BCrypt加密（推荐）
 * String encodedPassword = passwordEncoder.encode("password123");
 * boolean matches = passwordEncoder.matches("password123", encodedPassword);
 * 
 * // 检查密码强度
 * boolean isStrong = passwordEncoder.isStrongPassword("Password123!");
 * }</pre>
 * 
 * <h2>配置说明</h2>
 * 
 * <h3>JWT配置</h3>
 * <ul>
 *   <li><code>muying.security.jwt.secret</code> - JWT签名密钥</li>
 *   <li><code>muying.security.jwt.expiration</code> - 令牌过期时间（秒）</li>
 *   <li><code>muying.security.jwt.refresh-expiration</code> - 刷新令牌过期时间（秒）</li>
 * </ul>
 * 
 * <h3>密码策略配置</h3>
 * <ul>
 *   <li><code>muying.security.password.policy.min-length</code> - 最小长度</li>
 *   <li><code>muying.security.password.policy.require-uppercase</code> - 是否要求大写字母</li>
 *   <li><code>muying.security.password.policy.require-digit</code> - 是否要求数字</li>
 * </ul>
 * 
 * <h3>CORS配置</h3>
 * <ul>
 *   <li><code>muying.security.cors.enabled</code> - 是否启用CORS</li>
 *   <li><code>muying.security.cors.allowed-origins</code> - 允许的源</li>
 *   <li><code>muying.security.cors.allowed-methods</code> - 允许的方法</li>
 * </ul>
 * 
 * <h2>最佳实践</h2>
 * <ul>
 *   <li>使用强密钥配置JWT签名</li>
 *   <li>定期更新JWT密钥</li>
 *   <li>启用密码策略验证</li>
 *   <li>使用BCrypt算法加密密码</li>
 *   <li>合理设置令牌过期时间</li>
 *   <li>在生产环境中禁用CORS的通配符配置</li>
 * </ul>
 * 
 * <h2>注意事项</h2>
 * <ul>
 *   <li>该模块需要Spring Boot 3.x环境</li>
 *   <li>JWT功能需要相关依赖支持</li>
 *   <li>安全注解需要配合拦截器或AOP使用</li>
 *   <li>密码加密服务支持多种算法</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0.0
 */
package com.muyingmall.common.security;