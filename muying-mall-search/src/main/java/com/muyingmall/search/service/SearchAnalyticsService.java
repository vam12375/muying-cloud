package com.muyingmall.search.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 搜索分析服务接口
 * 提供搜索数据统计和分析功能
 */
public interface SearchAnalyticsService {

    /**
     * 记录搜索行为
     * @param keyword 搜索关键词
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param resultCount 搜索结果数
     * @param searchTime 搜索响应时间（毫秒）
     */
    void recordSearch(String keyword, Integer userId, String sessionId, 
                     long resultCount, long searchTime);

    /**
     * 记录搜索点击行为
     * @param keyword 搜索关键词
     * @param productId 点击的商品ID
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param position 商品在搜索结果中的位置
     */
    void recordSearchClick(String keyword, Integer productId, Integer userId, 
                          String sessionId, int position);

    /**
     * 获取热门搜索关键词统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回数量
     * @return 热门关键词列表
     */
    List<Map<String, Object>> getHotKeywords(LocalDateTime startTime, 
                                           LocalDateTime endTime, int limit);

    /**
     * 获取搜索趋势数据
     * @param keyword 关键词
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param granularity 粒度（hour, day, week, month）
     * @return 趋势数据
     */
    List<Map<String, Object>> getSearchTrend(String keyword, LocalDateTime startTime, 
                                            LocalDateTime endTime, String granularity);

    /**
     * 获取零搜索结果关键词
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回数量
     * @return 零结果关键词列表
     */
    List<Map<String, Object>> getZeroResultKeywords(LocalDateTime startTime, 
                                                   LocalDateTime endTime, int limit);

    /**
     * 获取搜索转化率统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 转化率数据
     */
    Map<String, Object> getSearchConversionRate(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户搜索行为分析
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户搜索行为数据
     */
    Map<String, Object> getUserSearchBehavior(Integer userId, LocalDateTime startTime, 
                                             LocalDateTime endTime);

    /**
     * 获取搜索性能统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 性能统计数据
     */
    Map<String, Object> getSearchPerformanceStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取搜索建议优化数据
     * @return 优化建议数据
     */
    Map<String, Object> getSearchOptimizationData();

    /**
     * 导出搜索统计报告
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param reportType 报告类型
     * @return 报告数据
     */
    Map<String, Object> exportSearchReport(LocalDateTime startTime, LocalDateTime endTime, 
                                         String reportType);
}