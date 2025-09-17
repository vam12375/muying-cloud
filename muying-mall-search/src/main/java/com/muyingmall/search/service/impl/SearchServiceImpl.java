package com.muyingmall.search.service.impl;

import com.muyingmall.common.dto.PageResult;
import com.muyingmall.search.document.ProductDocument;
import com.muyingmall.search.dto.SearchRequest;
import com.muyingmall.search.repository.ProductSearchRepository;
import com.muyingmall.search.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public PageResult<ProductDocument> searchProducts(SearchRequest request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // Keyword search
        if (StringUtils.hasText(request.getKeyword())) {
            boolQuery.should(QueryBuilders.matchQuery("name", request.getKeyword()).boost(2.0f))
                    .should(QueryBuilders.matchQuery("description", request.getKeyword()))
                    .minimumShouldMatch(1);
        }

        // Category filter
        if (StringUtils.hasText(request.getCategoryName())) {
            boolQuery.must(QueryBuilders.termQuery("categoryName", request.getCategoryName()));
        }

        // Brand filter
        if (StringUtils.hasText(request.getBrandName())) {
            boolQuery.must(QueryBuilders.termQuery("brandName", request.getBrandName()));
        }

        // Price range filter
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            var rangeQuery = QueryBuilders.rangeQuery("price");
            if (request.getMinPrice() != null) {
                rangeQuery.gte(request.getMinPrice());
            }
            if (request.getMaxPrice() != null) {
                rangeQuery.lte(request.getMaxPrice());
            }
            boolQuery.must(rangeQuery);
        }

        // Only active products
        boolQuery.must(QueryBuilders.termQuery("status", true));

        // Sort
        Sort sort = getSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(searchQuery, ProductDocument.class);
        List<ProductDocument> products = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageResult<>(
                products,
                searchHits.getTotalHits(),
                request.getPage(),
                request.getSize()
        );
    }

    @Override
    public void indexProduct(ProductDocument product) {
        productSearchRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productSearchRepository.deleteById(productId);
    }

    @Override
    public void updateProduct(ProductDocument product) {
        productSearchRepository.save(product);
    }

    @Override
    public List<String> getSuggestions(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return new ArrayList<>();
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhrasePrefixQuery("name", keyword))
                .should(QueryBuilders.matchPhrasePrefixQuery("description", keyword))
                .must(QueryBuilders.termQuery("status", true));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 10))
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchTemplate.search(searchQuery, ProductDocument.class);

        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent().getName())
                .distinct()
                .collect(Collectors.toList());
    }

    private Sort getSort(String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "salesCount");
        }

        switch (sortBy) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "sales_desc":
                return Sort.by(Sort.Direction.DESC, "salesCount");
            case "rating_desc":
                return Sort.by(Sort.Direction.DESC, "rating");
            case "created_desc":
                return Sort.by(Sort.Direction.DESC, "createdTime");
            default:
                return Sort.by(Sort.Direction.DESC, "salesCount");
        }
    }
}