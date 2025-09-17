package com.muyingmall.logistics.service;

import com.muyingmall.logistics.dto.TrackingInfoDto;
import com.muyingmall.logistics.entity.LogisticsTrack;

import java.util.List;
import java.util.Map;

/**
 * 第三方物流服务接口
 * 负责与各大物流公司API对接
 */
public interface ThirdPartyLogisticsService {

    /**
     * 查询物流轨迹信息
     * @param companyCode 物流公司代码（如：SF、YTO、ZTO、STO等）
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryTrackingInfo(String companyCode, String trackingNo);

    /**
     * 查询顺丰物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> querySFExpress(String trackingNo);

    /**
     * 查询圆通物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryYTOExpress(String trackingNo);

    /**
     * 查询中通物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryZTOExpress(String trackingNo);

    /**
     * 查询申通物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> querySTOExpress(String trackingNo);

    /**
     * 查询韵达物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryYDExpress(String trackingNo);

    /**
     * 查询极兔物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryJTExpress(String trackingNo);

    /**
     * 查询京东物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryJDExpress(String trackingNo);

    /**
     * 查询邮政EMS物流信息
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryEMSExpress(String trackingNo);

    /**
     * 创建物流订单
     * @param companyCode 物流公司代码
     * @param orderInfo 订单信息
     * @return 创建结果，包含物流单号等信息
     */
    Map<String, Object> createLogisticsOrder(String companyCode, Map<String, Object> orderInfo);

    /**
     * 取消物流订单
     * @param companyCode 物流公司代码
     * @param trackingNo 物流单号
     * @return 取消结果
     */
    boolean cancelLogisticsOrder(String companyCode, String trackingNo);

    /**
     * 获取运费报价
     * @param companyCode 物流公司代码
     * @param fromCityCode 发货城市代码
     * @param toCityCode 收货城市代码
     * @param weight 重量（kg）
     * @param volume 体积（立方厘米）
     * @return 运费信息
     */
    Map<String, Object> getShippingRate(String companyCode, String fromCityCode, 
                                       String toCityCode, Double weight, Double volume);

    /**
     * 验证物流单号格式
     * @param companyCode 物流公司代码
     * @param trackingNo 物流单号
     * @return 是否有效
     */
    boolean validateTrackingNo(String companyCode, String trackingNo);

    /**
     * 获取支持的物流公司列表
     * @return 物流公司信息列表
     */
    List<Map<String, Object>> getSupportedLogisticsCompanies();

    /**
     * 统一查询接口（快递100、快递鸟等）
     * @param provider 查询服务提供商
     * @param companyCode 物流公司代码
     * @param trackingNo 物流单号
     * @return 物流轨迹信息
     */
    List<LogisticsTrack> queryByThirdParty(String provider, String companyCode, String trackingNo);

    /**
     * 订阅物流状态变更通知
     * @param companyCode 物流公司代码
     * @param trackingNo 物流单号
     * @param callbackUrl 回调地址
     * @return 订阅结果
     */
    boolean subscribeStatusNotify(String companyCode, String trackingNo, String callbackUrl);

    /**
     * 处理物流状态变更回调
     * @param provider 服务提供商
     * @param callbackData 回调数据
     * @return 处理结果
     */
    boolean handleStatusCallback(String provider, Map<String, Object> callbackData);

    /**
     * 获取物流异常信息
     * @param companyCode 物流公司代码
     * @param trackingNo 物流单号
     * @return 异常信息
     */
    List<Map<String, Object>> getLogisticsExceptions(String companyCode, String trackingNo);
}