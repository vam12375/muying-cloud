package com.muyingmall.user.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 用户资料更新DTO
 */
@Data
public class UserProfileDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 (0-未知, 1-男, 2-女)
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;
}