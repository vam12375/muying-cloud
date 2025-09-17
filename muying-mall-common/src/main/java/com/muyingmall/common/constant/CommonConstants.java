package com.muyingmall.common.constant;

public class CommonConstants {
    
    public static final String DEFAULT_USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;
    
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    public static final String DEFAULT_SORT_FIELD = "createTime";
    
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String ERROR_MESSAGE = "操作失败";
    
    public static final String LOGIN_SUCCESS_MESSAGE = "登录成功";
    public static final String LOGIN_FAILED_MESSAGE = "用户名或密码错误";
    public static final String LOGOUT_SUCCESS_MESSAGE = "退出成功";
    
    public static final String UNAUTHORIZED_MESSAGE = "未授权访问";
    public static final String FORBIDDEN_MESSAGE = "访问被拒绝";
    public static final String NOT_FOUND_MESSAGE = "资源不存在";
    
    public static final String VALIDATION_FAILED_MESSAGE = "参数校验失败";
    public static final String SYSTEM_ERROR_MESSAGE = "系统异常";
    
    private CommonConstants() {
    }
}