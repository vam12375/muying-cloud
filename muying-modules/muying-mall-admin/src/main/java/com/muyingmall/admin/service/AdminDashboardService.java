package com.muyingmall.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 管理后台数据统计服务接口
 * 提供完整的后台数据分析和统计功能
 */
public interface AdminDashboardService {

    /**
     * 获取首页仪表盘数据
     * @return 仪表盘数据
     */
    Map<String, Object> getDashboardData();

    /**
     * 获取销售统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param granularity 时间粒度（day, week, month）
     * @return 销售统计数据
     */
    Map<String, Object> getSalesStats(LocalDateTime startTime, LocalDateTime endTime, String granularity);

    /**
     * 获取用户统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户统计数据
     */
    Map<String, Object> getUserStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取商品统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品统计数据
     */
    Map<String, Object> getProductStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取订单统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单统计数据
     */
    Map<String, Object> getOrderStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取支付统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 支付统计数据
     */
    Map<String, Object> getPaymentStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取热门商品排行
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 排行类型（sales, views, favorites）
     * @param limit 返回数量
     * @return 商品排行数据
     */
    List<Map<String, Object>> getHotProductsRanking(LocalDateTime startTime, LocalDateTime endTime, 
                                                   String type, Integer limit);

    /**
     * 获取用户活跃度分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户活跃度数据
     */
    Map<String, Object> getUserActivityAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取地区销售分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 地区销售数据
     */
    Map<String, Object> getRegionalSalesAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取退款退货统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 退款退货统计数据
     */
    Map<String, Object> getRefundStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取库存预警数据
     * @param threshold 库存预警阈值
     * @return 库存预警数据
     */
    List<Map<String, Object>> getInventoryAlerts(Integer threshold);

    /**
     * 获取系统健康状态
     * @return 系统健康状态数据
     */
    Map<String, Object> getSystemHealthStatus();

    /**
     * 获取实时数据
     * @return 实时数据
     */
    Map<String, Object> getRealtimeData();

    /**
     * 获取转化率分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 转化率数据
     */
    Map<String, Object> getConversionAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取客户价值分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 客户价值分析数据
     */
    Map<String, Object> getCustomerValueAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取营销活动效果分析
     * @param campaignId 活动ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 营销效果数据
     */
    Map<String, Object> getCampaignEffectiveness(Long campaignId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取商品分类销售分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分类销售数据
     */
    Map<String, Object> getCategorySalesAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取流失用户分析
     * @param inactiveDays 无活动天数阈值
     * @return 流失用户数据
     */
    Map<String, Object> getChurnUserAnalysis(Integer inactiveDays);

    /**
     * 获取复购率分析
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 复购率数据
     */
    Map<String, Object> getRepurchaseRateAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取评价分析数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 评价分析数据
     */
    Map<String, Object> getReviewAnalysis(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取搜索热词统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回数量
     * @return 搜索热词数据
     */
    List<Map<String, Object>> getSearchHotWords(LocalDateTime startTime, LocalDateTime endTime, Integer limit);

    /**
     * 导出统计报告
     * @param reportType 报告类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param format 导出格式（excel, pdf, csv）
     * @return 报告文件路径或数据
     */
    String exportStatisticsReport(String reportType, LocalDateTime startTime, 
                                 LocalDateTime endTime, String format);

    /**
     * 获取异常数据监控
     * @return 异常数据列表
     */
    List<Map<String, Object>> getAnomalyMonitoring();

    /**
     * 获取预测分析数据
     * @param type 预测类型（sales, users, products）
     * @param days 预测天数
     * @return 预测数据
     */
    Map<String, Object> getPredictionAnalysis(String type, Integer days);
}