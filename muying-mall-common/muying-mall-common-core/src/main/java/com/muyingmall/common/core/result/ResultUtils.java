package com.muyingmall.common.core.result;

import com.muyingmall.common.core.enums.ErrorCode;
import com.muyingmall.common.core.enums.ResultCode;

import java.util.List;
import java.util.function.Supplier;

/**
 * 响应结果工具类
 * 提供常用的结果处理方法
 *
 * @author 青柠檬
 * @since 2025-09-23
 */
public class ResultUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private ResultUtils() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    // ==================== 成功响应 ====================

    /**
     * 创建成功响应
     *
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 创建成功响应
     *
     * @param message 响应消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    // ==================== 失败响应 ====================

    /**
     * 创建失败响应
     *
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error() {
        return Result.error();
    }

    /**
     * 创建失败响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(String message) {
        return Result.error(message);
    }

    /**
     * 创建失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        return Result.error(code, message);
    }

    /**
     * 创建失败响应（使用ResultCode枚举）
     *
     * @param resultCode 结果码枚举
     * @param <T>        数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return Result.error(resultCode);
    }

    /**
     * 创建失败响应（使用ErrorCode枚举）
     *
     * @param errorCode 错误码枚举
     * @param <T>       数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return Result.error(errorCode);
    }

    // ==================== 条件响应 ====================

    /**
     * 根据条件返回成功或失败响应
     *
     * @param condition 条件
     * @param data      成功时的数据
     * @param message   失败时的消息
     * @param <T>       数据类型
     * @return Result对象
     */
    public static <T> Result<T> condition(boolean condition, T data, String message) {
        return condition ? success(data) : error(message);
    }

    /**
     * 根据条件返回成功或失败响应
     *
     * @param condition    条件
     * @param successData  成功时的数据
     * @param errorCode    失败时的错误码
     * @param <T>          数据类型
     * @return Result对象
     */
    public static <T> Result<T> condition(boolean condition, T successData, ErrorCode errorCode) {
        return condition ? success(successData) : error(errorCode);
    }

    /**
     * 根据数据是否为空返回响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return Result对象
     */
    public static <T> Result<T> ofNullable(T data) {
        return data != null ? success(data) : error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
    }

    /**
     * 根据数据是否为空返回响应（自定义空值消息）
     *
     * @param data         数据
     * @param emptyMessage 空值时的消息
     * @param <T>          数据类型
     * @return Result对象
     */
    public static <T> Result<T> ofNullable(T data, String emptyMessage) {
        return data != null ? success(data) : error(emptyMessage);
    }

    /**
     * 根据列表是否为空返回响应
     *
     * @param list 列表数据
     * @param <T>  数据类型
     * @return Result对象
     */
    public static <T> Result<List<T>> ofList(List<T> list) {
        return (list != null && !list.isEmpty()) ? 
            success(list) : 
            error(ResultCode.NOT_FOUND.getCode(), "暂无数据");
    }

    // ==================== 异常处理 ====================

    /**
     * 安全执行操作，捕获异常并返回结果
     *
     * @param supplier 操作供应商
     * @param <T>      数据类型
     * @return Result对象
     */
    public static <T> Result<T> execute(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return success(result);
        } catch (Exception e) {
            return error("操作失败: " + e.getMessage());
        }
    }

    /**
     * 安全执行操作，捕获异常并返回结果（自定义错误消息）
     *
     * @param supplier     操作供应商
     * @param errorMessage 错误消息
     * @param <T>          数据类型
     * @return Result对象
     */
    public static <T> Result<T> execute(Supplier<T> supplier, String errorMessage) {
        try {
            T result = supplier.get();
            return success(result);
        } catch (Exception e) {
            return error(errorMessage);
        }
    }

    // ==================== 分页响应 ====================

    /**
     * 创建分页成功响应
     *
     * @param pageResult 分页结果
     * @param <T>        数据类型
     * @return Result对象
     */
    public static <T> Result<PageResult<T>> page(PageResult<T> pageResult) {
        return success(pageResult);
    }

    /**
     * 创建分页成功响应（自定义消息）
     *
     * @param pageResult 分页结果
     * @param message    响应消息
     * @param <T>        数据类型
     * @return Result对象
     */
    public static <T> Result<PageResult<T>> page(PageResult<T> pageResult, String message) {
        return success(message, pageResult);
    }

    /**
     * 创建空分页响应
     *
     * @param page 页码
     * @param size 每页大小
     * @param <T>  数据类型
     * @return Result对象
     */
    public static <T> Result<PageResult<T>> emptyPage(int page, int size) {
        return success(PageResult.empty(page, size));
    }

    // ==================== 结果转换 ====================

    /**
     * 将Result转换为ApiResponse
     *
     * @param result Result对象
     * @param <T>    数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> toApiResponse(Result<T> result) {
        return ApiResponse.from(result);
    }

    /**
     * 判断结果是否成功
     *
     * @param result Result对象
     * @return 是否成功
     */
    public static boolean isSuccess(Result<?> result) {
        return result != null && result.isSuccess();
    }

    /**
     * 判断结果是否失败
     *
     * @param result Result对象
     * @return 是否失败
     */
    public static boolean isError(Result<?> result) {
        return result == null || result.isError();
    }

    /**
     * 获取结果数据，如果失败则返回默认值
     *
     * @param result       Result对象
     * @param defaultValue 默认值
     * @param <T>          数据类型
     * @return 数据或默认值
     */
    public static <T> T getDataOrDefault(Result<T> result, T defaultValue) {
        return isSuccess(result) ? result.getData() : defaultValue;
    }
}