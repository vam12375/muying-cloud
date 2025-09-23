/**
 * 通用工具类包
 * 
 * <p>提供各种通用的工具类，包括：</p>
 * <ul>
 *   <li>{@link com.muyingmall.common.core.utils.JwtUtils} - JWT令牌工具类</li>
 *   <li>{@link com.muyingmall.common.core.utils.RedisUtils} - Redis缓存工具类</li>
 *   <li>{@link com.muyingmall.common.core.utils.EnumUtils} - 枚举工具类</li>
 *   <li>{@link com.muyingmall.common.core.utils.SpringContextUtils} - Spring上下文工具类</li>
 *   <li>{@link com.muyingmall.common.core.utils.SecurityUtils} - 安全工具类</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // JWT操作
 * String token = jwtUtils.generateToken(1001, "admin", "ADMIN");
 * boolean isValid = jwtUtils.validateToken(token);
 * 
 * // Redis操作
 * redisUtils.set("key", "value", 3600);
 * Object value = redisUtils.get("key");
 * 
 * // 枚举操作
 * OrderStatus status = EnumUtils.getOrderStatusByCode("PAID");
 * 
 * // Spring Bean获取
 * UserService userService = SpringContextUtils.getBean(UserService.class);
 * 
 * // 安全操作
 * String hashedPassword = SecurityUtils.hashPassword("password", salt);
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>所有工具类都是无状态的，可以安全地在多线程环境中使用</li>
 *   <li>部分工具类依赖Spring环境，使用前请确保相关依赖已配置</li>
 *   <li>Redis相关工具类需要Redis服务可用</li>
 *   <li>JWT工具类需要配置密钥和过期时间</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
package com.muyingmall.common.core.utils;