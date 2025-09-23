package com.muyingmall.common.dto;

/**
 * @deprecated 此类已迁移到 com.muyingmall.common.core.result.PageResult
 * 请使用新的包路径，此类将在后续版本中移除
 */
@Deprecated
public class PageResult<T> extends com.muyingmall.common.core.result.PageResult<T> {
    
    public PageResult() {
        super();
    }
    
    public PageResult(java.util.List<T> content, int page, int size, long total) {
        super(content, page, size, total);
    }
}
