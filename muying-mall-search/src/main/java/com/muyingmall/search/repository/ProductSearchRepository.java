package com.muyingmall.search.repository;

import com.muyingmall.search.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    Page<ProductDocument> findByNameContainingOrDescriptionContaining(
            String name, String description, Pageable pageable);

    Page<ProductDocument> findByCategoryName(String categoryName, Pageable pageable);

    Page<ProductDocument> findByBrandName(String brandName, Pageable pageable);

    Page<ProductDocument> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<ProductDocument> findByStatusTrue(Pageable pageable);
}