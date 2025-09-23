/**
 * 响应结果封装包
 * 
 * <p>提供统一的API响应格式封装，包括：</p>
 * <ul>
 *   <li>{@link com.muyingmall.common.core.result.Result} - 标准响应结果封装</li>
 *   <li>{@link com.muyingmall.common.core.result.ApiResponse} - API响应封装（继承Result，增加success字段）</li>
 *   <li>{@link com.muyingmall.common.core.result.PageResult} - 分页结果封装</li>
 *   <li>{@link com.muyingmall.common.core.result.ResultBuilder} - 响应结果构建器</li>
 *   <li>{@link com.muyingmall.common.core.result.ResultUtils} - 响应结果工具类</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // 成功响应
 * Result<User> result = Result.success(user);
 * Result<User> result = ResultUtils.success(user);
 * 
 * // 失败响应
 * Result<Void> result = Result.error("操作失败");
 * Result<Void> result = ResultUtils.error(ErrorCode.BUSINESS_ERROR);
 * 
 * // 使用构建器
 * Result<User> result = ResultBuilder.<User>create()
 *     .success()
 *     .message("用户创建成功")
 *     .data(user)
 *     .build();
 * 
 * // 分页响应
 * PageResult<User> pageResult = PageResult.of(userList, 1, 10, 100);
 * Result<PageResult<User>> result = ResultUtils.page(pageResult);
 * 
 * // API响应
 * ApiResponse<User> apiResponse = ApiResponse.success(user);
 * }</pre>
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
package com.muyingmall.common.core.result;