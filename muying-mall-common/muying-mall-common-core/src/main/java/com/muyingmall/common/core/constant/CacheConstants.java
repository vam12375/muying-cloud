package com.muyingmall.common.core.constant;

/**
 * 缓存常量类
 * 定义系统中使用的各种缓存键名和过期时间
 *
 * @author 青柠檬
 * @since 2025-09-25
 */
public class CacheConstants {

    /**
     * 用户积分状态缓存键前缀
     */
    public static final String USER_POINTS_STATUS_KEY = "user:points:status:";

    /**
     * 签到状态缓存键前缀
     */
    public static final String SIGNIN_STATUS_KEY = "user:signin:status:";

    /**
     * 签到状态过期时间（秒）- 24小时
     */
    public static final long SIGNIN_STATUS_EXPIRE_TIME = 24 * 60 * 60;

    /**
     * 用户信息缓存键前缀
     */
    public static final String USER_INFO_KEY = "user:info:";

    /**
     * 用户信息缓存过期时间（秒）- 30分钟
     */
    public static final long USER_INFO_EXPIRE_TIME = 30 * 60;

    /**
     * 积分排行榜缓存键
     */
    public static final String POINTS_RANKING_KEY = "points:ranking";

    /**
     * 积分排行榜缓存过期时间（秒）- 1小时
     */
    public static final long POINTS_RANKING_EXPIRE_TIME = 60 * 60;

    /**
     * 用户钱包缓存键前缀
     */
    public static final String USER_WALLET_KEY = "user:wallet:";

    /**
     * 用户钱包缓存过期时间（秒）- 10分钟
     */
    public static final long USER_WALLET_EXPIRE_TIME = 10 * 60;

    /**
     * 防止缓存穿透的空值标识
     */
    public static final String NULL_VALUE = "NULL";

    /**
     * 空值缓存过期时间（秒）- 5分钟
     */
    public static final long NULL_VALUE_EXPIRE_TIME = 5 * 60;
}