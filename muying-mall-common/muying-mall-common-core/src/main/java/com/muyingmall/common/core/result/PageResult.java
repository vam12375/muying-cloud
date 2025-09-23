package com.muyingmall.common.core.result;

import lombok.Data;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Collections;

/**
 * 分页结果封装类
 * 解决Spring Data Page对象序列化问题，提供统一的分页结果格式
 *
 * @param <T> 数据类型
 * @author 青柠檬
 * @since 2025-09-23
 */
@Data
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据列表
     */
    private List<T> content;
    
    /**
     * 当前页码（从1开始，与前端保持一致）
     */
    private int page;
    
    /**
     * 每页大小
     */
    private int size;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 是否为第一页
     */
    private boolean first;
    
    /**
     * 是否为最后一页
     */
    private boolean last;
    
    /**
     * 是否有下一页
     */
    private boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private boolean hasPrevious;
    
    /**
     * 当前页的元素数量
     */
    private int numberOfElements;
    
    /**
     * 是否为空页
     */
    private boolean empty;
    
    /**
     * 默认构造函数
     */
    public PageResult() {
        this.content = Collections.emptyList();
        this.page = 1;
        this.size = 10;
        this.total = 0;
        this.totalPages = 0;
        this.numberOfElements = 0;
        this.first = true;
        this.last = true;
        this.hasNext = false;
        this.hasPrevious = false;
        this.empty = true;
    }
    
    /**
     * 构造函数
     *
     * @param content 数据列表
     * @param page    当前页码（从1开始）
     * @param size    每页大小
     * @param total   总记录数
     */
    public PageResult(List<T> content, int page, int size, long total) {
        this.content = content != null ? content : Collections.emptyList();
        this.page = Math.max(page, 1);
        this.size = Math.max(size, 1);
        this.total = Math.max(total, 0);
        this.totalPages = size == 0 ? 1 : (int) Math.ceil((double) total / (double) size);
        this.numberOfElements = this.content.size();
        this.first = this.page == 1;
        this.last = this.page >= this.totalPages;
        this.hasNext = this.page < this.totalPages;
        this.hasPrevious = this.page > 1;
        this.empty = this.numberOfElements == 0;
    }
    
    /**
     * 从Spring Data Page对象转换（页码从0开始转换为从1开始）
     * 注意：此方法需要在具有Spring Data依赖的模块中使用
     *
     * @param pageContent    页面内容
     * @param pageNumber     页码（从0开始）
     * @param pageSize       每页大小
     * @param totalElements  总元素数
     * @param <T>            数据类型
     * @return PageResult对象
     */
    public static <T> PageResult<T> fromSpringData(List<T> pageContent, int pageNumber, int pageSize, long totalElements) {
        return new PageResult<>(
                pageContent,
                pageNumber + 1, // Spring Data页码从0开始，转换为从1开始
                pageSize,
                totalElements
        );
    }
    
    /**
     * 从MyBatis Plus IPage对象转换
     *
     * @param page MyBatis Plus IPage对象
     * @param <T>  数据类型
     * @return PageResult对象
     */
    public static <T> PageResult<T> from(IPage<T> page) {
        if (page == null) {
            return new PageResult<>();
        }
        
        return new PageResult<>(
                page.getRecords(),
                (int) page.getCurrent(), // MyBatis Plus页码从1开始
                (int) page.getSize(),
                page.getTotal()
        );
    }
    
    /**
     * 创建空的分页结果
     *
     * @param <T> 数据类型
     * @return 空的PageResult对象
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>();
    }
    
    /**
     * 创建空的分页结果，指定页码和大小
     *
     * @param page 页码
     * @param size 每页大小
     * @param <T>  数据类型
     * @return 空的PageResult对象
     */
    public static <T> PageResult<T> empty(int page, int size) {
        return new PageResult<>(Collections.emptyList(), page, size, 0);
    }
    
    /**
     * 创建单页结果（不分页）
     *
     * @param content 数据列表
     * @param <T>     数据类型
     * @return PageResult对象
     */
    public static <T> PageResult<T> of(List<T> content) {
        if (content == null || content.isEmpty()) {
            return empty();
        }
        return new PageResult<>(content, 1, content.size(), content.size());
    }
    
    /**
     * 创建分页结果
     *
     * @param content 数据列表
     * @param page    当前页码
     * @param size    每页大小
     * @param total   总记录数
     * @param <T>     数据类型
     * @return PageResult对象
     */
    public static <T> PageResult<T> of(List<T> content, int page, int size, long total) {
        return new PageResult<>(content, page, size, total);
    }
}