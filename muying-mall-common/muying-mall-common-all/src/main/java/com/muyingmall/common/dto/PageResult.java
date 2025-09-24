package com.muyingmall.common.dto;

import java.util.List;

/**
 * 向后兼容类 - PageResult
 * 
 * @deprecated 请使用 {@link com.muyingmall.common.core.result.PageResult} 替代
 * @author 青柠檬
 * @since 2025-09-24
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class PageResult<T> {
    
    private List<T> content;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    
    /**
     * 默认构造函数
     */
    public PageResult() {
    }
    
    /**
     * 构造函数
     * 
     * @param data 数据列表
     * @param total 总数
     * @param pageNum 页码
     * @param pageSize 页大小
     */
    public PageResult(List<T> data, Long total, Integer pageNum, Integer pageSize) {
        this.content = data;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    /**
     * 创建分页结果
     * 
     * @param data 数据列表
     * @param total 总数
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param <T> 数据类型
     * @return PageResult实例
     */
    public static <T> PageResult<T> of(List<T> data, Long total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(data, total, pageNum, pageSize);
    }
    
    // Getters and Setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}