/**
 * 母婴商城公共模块 - Redis缓存模块
 * 
 * <p>本模块提供基于Redis的缓存功能，包括：</p>
 * 
 * <h3>核心功能</h3>
 * <ul>
 *   <li><strong>缓存服务</strong> - 提供统一的缓存操作接口</li>
 *   <li><strong>分布式锁</strong> - 基于Redis的分布式锁实现</li>
 *   <li><strong>工具类</strong> - 便捷的Redis操作工具</li>
 *   <li><strong>自动配置</strong> - Spring Boot自动配置支持</li>
 *   <li><strong>注解支持</strong> - 声明式缓存注解</li>
 * </ul>
 * 
 * <h3>主要组件</h3>
 * <ul>
 *   <li>{@link com.muyingmall.common.redis.service.CacheService} - 缓存服务接口</li>
 *   <li>{@link com.muyingmall.common.redis.service.impl.RedisCacheService} - Redis缓存服务实现</li>
 *   <li>{@link com.muyingmall.common.redis.lock.DistributedLock} - 分布式锁接口</li>
 *   <li>{@link com.muyingmall.common.redis.lock.impl.RedisDistributedLock} - Redis分布式锁实现</li>
 *   <li>{@link com.muyingmall.common.redis.utils.RedisUtils} - Redis工具类</li>
 *   <li>{@link com.muyingmall.common.redis.config.RedisAutoConfiguration} - 自动配置类</li>
 * </ul>
 * 
 * <h3>配置属性</h3>
 * <p>配置前缀：{@code muying.redis}</p>
 * <pre>{@code
 * muying:
 *   redis:
 *     key-prefix: "muying:"           # 缓存键前缀
 *     default-expire: PT1H            # 默认过期时间（1小时）
 *     enable-key-prefix: true         # 是否启用键前缀
 *     lock:
 *       default-expire-time: 30       # 默认锁过期时间（秒）
 *       retry-count: 3                # 获取锁重试次数
 *       retry-interval: 100           # 重试间隔（毫秒）
 *       key-prefix: "lock:"           # 锁键前缀
 *     cache:
 *       cache-null-values: true       # 是否缓存空值
 *       null-value-expire-time: 300   # 空值缓存时间（秒）
 *       enable-statistics: false      # 是否启用缓存统计
 * }</pre>
 * 
 * <h3>使用示例</h3>
 * 
 * <h4>基本缓存操作</h4>
 * <pre>{@code
 * @Autowired
 * private CacheService cacheService;
 * 
 * // 设置缓存
 * cacheService.set("user:1001", userInfo, Duration.ofHours(1));
 * 
 * // 获取缓存
 * UserInfo user = cacheService.get("user:1001", UserInfo.class);
 * 
 * // 删除缓存
 * cacheService.delete("user:1001");
 * }</pre>
 * 
 * <h4>分布式锁使用</h4>
 * <pre>{@code
 * @Autowired
 * private DistributedLock distributedLock;
 * 
 * String lockKey = "order:lock:1001";
 * String requestId = UUID.randomUUID().toString();
 * 
 * if (distributedLock.tryLock(lockKey, requestId, 30, TimeUnit.SECONDS)) {
 *     try {
 *         // 执行业务逻辑
 *     } finally {
 *         distributedLock.unlock(lockKey, requestId);
 *     }
 * }
 * }</pre>
 * 
 * <h4>工具类使用</h4>
 * <pre>{@code
 * // 基本操作
 * RedisUtils.set("key", "value", Duration.ofMinutes(30));
 * String value = RedisUtils.get("key", String.class);
 * 
 * // Hash操作
 * RedisUtils.hSet("user:profile:1001", "name", "张三");
 * String name = (String) RedisUtils.hGet("user:profile:1001", "name");
 * 
 * // 原子操作
 * Long count = RedisUtils.increment("page:view:count");
 * }</pre>
 * 
 * <h4>注解使用</h4>
 * <pre>{@code
 * @Cacheable(key = "user:#{#userId}", expire = 3600)
 * public User getUserById(Long userId) {
 *     return userRepository.findById(userId);
 * }
 * 
 * @CacheEvict(key = "user:#{#userId}")
 * public void updateUser(Long userId, User user) {
 *     userRepository.update(user);
 * }
 * }</pre>
 * 
 * <h3>依赖关系</h3>
 * <ul>
 *   <li>依赖 {@code muying-mall-common-core} 模块</li>
 *   <li>依赖 {@code spring-boot-starter-data-redis}</li>
 *   <li>依赖 {@code commons-pool2} (连接池)</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>确保Redis服务器已启动并可连接</li>
 *   <li>分布式锁使用时请确保在finally块中释放锁</li>
 *   <li>缓存键建议使用有意义的前缀，避免键冲突</li>
 *   <li>合理设置缓存过期时间，避免内存溢出</li>
 *   <li>生产环境建议配置Redis连接池参数</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
package com.muyingmall.common.redis;