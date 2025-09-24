package com.muyingmall.search.service;

import com.muyingmall.common.core.result.PageResult;
import com.muyingmall.search.document.ProductDocument;
import com.muyingmall.search.dto.SearchRequest;

import java.util.List;

public interface SearchService {
    PageResult<ProductDocument> searchProducts(SearchRequest request);
    void indexProduct(ProductDocument product);
    void deleteProduct(Long productId);
    void updateProduct(ProductDocument product);
    List<String> getSuggestions(String keyword);
}