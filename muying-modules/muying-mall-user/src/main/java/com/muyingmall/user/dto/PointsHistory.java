package com.muyingmall.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 积分历史记录DTO
 */
@Data
public class PointsHistory {

    /**
     * 操作ID
     */
    private Long operationId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 操作类型描述
     */
    private String operationTypeDesc;

    /**
     * 积分变化数量
     */
    private Integer points;

    /**
     * 操作后余额
     */
    private Integer balanceAfter;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}