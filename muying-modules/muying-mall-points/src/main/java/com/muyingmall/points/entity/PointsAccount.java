package com.muyingmall.points.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 积分账户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("points_account")
public class PointsAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 总积分
     */
    private BigDecimal totalPoints;

    /**
     * 可用积分
     */
    private BigDecimal availablePoints;

    /**
     * 冻结积分
     */
    private BigDecimal frozenPoints;

    /**
     * 已使用积分
     */
    private BigDecimal usedPoints;

    /**
     * 积分等级
     */
    private Integer pointsLevel;

    /**
     * 账户状态 ACTIVE-正常 FROZEN-冻结 CLOSED-关闭
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}