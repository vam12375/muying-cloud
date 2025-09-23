package com.muyingmall.common.core.enums;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * 提供标准化的响应状态码和消息
 *
 * @author 青柠檬
 * @since 2025-09-23
 */
public enum ResultCode {
    
    // 成功状态码
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),
    ACCEPTED(202, "请求已接受"),
    NO_CONTENT(204, "无内容"),
    
    // 客户端错误状态码
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATE_FAILED(422, "参数检验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    
    // 服务器错误状态码
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    
    // 业务错误状态码
    BUSINESS_ERROR(600, "业务处理失败"),
    DATA_ERROR(601, "数据异常"),
    FLOW_LIMIT(602, "限流"),
    DEGRADATION(603, "降级"),
    
    // 兼容旧版本
    FAILED(500, "操作失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return ResultCode枚举，如果未找到返回null
     */
    public static ResultCode getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    /**
     * 判断是否为成功状态码
     *
     * @param code 状态码
     * @return 是否成功
     */
    public static boolean isSuccess(Integer code) {
        return code != null && code >= 200 && code < 300;
    }

    /**
     * 判断当前枚举是否为成功状态
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return isSuccess(this.code);
    }
}