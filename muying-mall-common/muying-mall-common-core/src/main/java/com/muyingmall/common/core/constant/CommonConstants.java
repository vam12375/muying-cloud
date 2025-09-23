package com.muyingmall.common.core.constant;

/**
 * 通用常量定义
 * 
 * @author muying-mall
 * @since 1.0.0
 */
public final class CommonConstants {
    
    // ==================== 用户角色常量 ====================
    
    /**
     * 默认用户角色
     */
    public static final String DEFAULT_USER_ROLE = "USER";
    
    /**
     * 管理员角色
     */
    public static final String ADMIN_ROLE = "ADMIN";
    
    // ==================== JWT相关常量 ====================
    
    /**
     * JWT请求头名称
     */
    public static final String JWT_HEADER_NAME = "Authorization";
    
    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    // ==================== 分页相关常量 ====================
    
    /**
     * 默认页面大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大页面大小
     */
    public static final Integer MAX_PAGE_SIZE = 100;
    
    /**
     * 默认排序方向
     */
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    
    /**
     * 默认排序字段
     */
    public static final String DEFAULT_SORT_FIELD = "createTime";
    
    // ==================== 响应消息常量 ====================
    
    /**
     * 成功消息
     */
    public static final String SUCCESS_MESSAGE = "操作成功";
    
    /**
     * 错误消息
     */
    public static final String ERROR_MESSAGE = "操作失败";
    
    /**
     * 登录成功消息
     */
    public static final String LOGIN_SUCCESS_MESSAGE = "登录成功";
    
    /**
     * 登录失败消息
     */
    public static final String LOGIN_FAILED_MESSAGE = "用户名或密码错误";
    
    /**
     * 退出成功消息
     */
    public static final String LOGOUT_SUCCESS_MESSAGE = "退出成功";
    
    /**
     * 未授权访问消息
     */
    public static final String UNAUTHORIZED_MESSAGE = "未授权访问";
    
    /**
     * 访问被拒绝消息
     */
    public static final String FORBIDDEN_MESSAGE = "访问被拒绝";
    
    /**
     * 资源不存在消息
     */
    public static final String NOT_FOUND_MESSAGE = "资源不存在";
    
    /**
     * 参数校验失败消息
     */
    public static final String VALIDATION_FAILED_MESSAGE = "参数校验失败";
    
    /**
     * 系统异常消息
     */
    public static final String SYSTEM_ERROR_MESSAGE = "系统异常";
    
    // ==================== 字符编码常量 ====================
    
    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * GBK编码
     */
    public static final String GBK = "GBK";
    
    // ==================== 日期时间格式常量 ====================
    
    /**
     * 标准日期时间格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 标准日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 标准时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";
    
    /**
     * 时间戳格式
     */
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    
    // ==================== 数字常量 ====================
    
    /**
     * 数字0
     */
    public static final Integer ZERO = 0;
    
    /**
     * 数字1
     */
    public static final Integer ONE = 1;
    
    /**
     * 数字-1
     */
    public static final Integer MINUS_ONE = -1;
    
    // ==================== 字符串常量 ====================
    
    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";
    
    /**
     * 逗号分隔符
     */
    public static final String COMMA = ",";
    
    /**
     * 点分隔符
     */
    public static final String DOT = ".";
    
    /**
     * 下划线分隔符
     */
    public static final String UNDERSCORE = "_";
    
    /**
     * 中划线分隔符
     */
    public static final String HYPHEN = "-";
    
    /**
     * 斜杠分隔符
     */
    public static final String SLASH = "/";
    
    /**
     * 反斜杠分隔符
     */
    public static final String BACKSLASH = "\\";
    
    // ==================== 布尔值常量 ====================
    
    /**
     * 是/启用
     */
    public static final String YES = "Y";
    
    /**
     * 否/禁用
     */
    public static final String NO = "N";
    
    /**
     * 启用状态
     */
    public static final Integer ENABLED = 1;
    
    /**
     * 禁用状态
     */
    public static final Integer DISABLED = 0;
    
    // ==================== 缓存相关常量 ====================
    
    /**
     * 缓存键分隔符
     */
    public static final String CACHE_KEY_SEPARATOR = ":";
    
    /**
     * 空值缓存标记
     */
    public static final String EMPTY_CACHE_VALUE = "EMPTY_VALUE_PROTECTION";
    
    /**
     * 私有构造函数，防止实例化
     */
    private CommonConstants() {
        throw new UnsupportedOperationException("常量类不能被实例化");
    }
}