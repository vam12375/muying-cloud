package com.muyingmall.common.redis.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 * 
 * <p>定义了分布式锁的标准操作接口，支持多种获取锁的方式。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>获取锁（阻塞和非阻塞）</li>
 *   <li>释放锁</li>
 *   <li>锁的自动过期</li>
 *   <li>重试机制</li>
 * </ul>
 * 
 * <p>使用示例：</p>
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
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
public interface DistributedLock {

    /**
     * 尝试获取锁（非阻塞）
     *
     * @param lockKey   锁的键
     * @param requestId 请求ID（用于标识锁的持有者）
     * @param expireTime 锁的过期时间
     * @param timeUnit   时间单位
     * @return 是否成功获取锁
     */
    boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit);

    /**
     * 尝试获取锁（非阻塞，使用默认过期时间）
     *
     * @param lockKey   锁的键
     * @param requestId 请求ID
     * @return 是否成功获取锁
     */
    boolean tryLock(String lockKey, String requestId);

    /**
     * 尝试获取锁（带重试）
     *
     * @param lockKey     锁的键
     * @param requestId   请求ID
     * @param expireTime  锁的过期时间
     * @param timeUnit    时间单位
     * @param retryCount  重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @return 是否成功获取锁
     */
    boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit, 
                   int retryCount, long retryInterval);

    /**
     * 获取锁（阻塞，直到获取成功或超时）
     *
     * @param lockKey    锁的键
     * @param requestId  请求ID
     * @param expireTime 锁的过期时间
     * @param timeUnit   时间单位
     * @param waitTime   等待时间
     * @return 是否成功获取锁
     * @throws InterruptedException 如果等待过程中被中断
     */
    boolean lock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit, long waitTime) 
            throws InterruptedException;

    /**
     * 释放锁
     *
     * @param lockKey   锁的键
     * @param requestId 请求ID（必须与获取锁时的requestId相同）
     * @return 是否成功释放锁
     */
    boolean unlock(String lockKey, String requestId);

    /**
     * 强制释放锁（不检查requestId）
     * 
     * <p>注意：此方法会强制释放锁，不管锁的持有者是谁，请谨慎使用。</p>
     *
     * @param lockKey 锁的键
     * @return 是否成功释放锁
     */
    boolean forceUnlock(String lockKey);

    /**
     * 检查锁是否存在
     *
     * @param lockKey 锁的键
     * @return 锁是否存在
     */
    boolean isLocked(String lockKey);

    /**
     * 获取锁的持有者
     *
     * @param lockKey 锁的键
     * @return 锁的持有者ID，如果锁不存在则返回null
     */
    String getLockHolder(String lockKey);

    /**
     * 续期锁（延长锁的过期时间）
     *
     * @param lockKey    锁的键
     * @param requestId  请求ID
     * @param expireTime 新的过期时间
     * @param timeUnit   时间单位
     * @return 是否成功续期
     */
    boolean renewLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit);
}