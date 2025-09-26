package com.muyingmall.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户积分实体类
 */
@Data
@TableName("user_points")
public class UserPoints implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 积分ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 当前积分余额
     */
    private Integer points;

    /**
     * 累计获得积分
     */
    private Integer totalEarned;

    /**
     * 累计使用积分
     */
    private Integer totalUsed;

    /**
     * 会员等级
     */
    private String level;

    // 兼容性方法
    public Integer getCurrentPoints() {
        return this.points;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.points = currentPoints;
    }

    public Integer getTotalEarned() {
        return this.totalEarned != null ? this.totalEarned : 0;
    }

    public void setTotalEarned(Integer totalEarned) {
        this.totalEarned = totalEarned;
    }

    public Integer getTotalUsed() {
        return this.totalUsed != null ? this.totalUsed : 0;
    }

    public void setTotalUsed(Integer totalUsed) {
        this.totalUsed = totalUsed;
    }

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户信息（非数据库字段）
     */
    @TableField(exist = false)
    private User user;
} 