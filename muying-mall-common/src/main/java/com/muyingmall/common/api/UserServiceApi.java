package com.muyingmall.common.api;

import com.muyingmall.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务API接口
 */
@FeignClient(name = "muying-mall-user", path = "/user")
public interface UserServiceApi {

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/info/{userId}")
    Result<UserInfoDto> getUserById(@PathVariable("userId") Long userId);

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/info/by-username/{username}")
    Result<UserInfoDto> getUserByUsername(@PathVariable("username") String username);

    /**
     * 验证用户是否存在
     */
    @GetMapping("/exists/{userId}")
    Result<Boolean> userExists(@PathVariable("userId") Long userId);

    /**
     * 获取用户地址信息
     */
    @GetMapping("/address/{userId}")
    Result<java.util.List<AddressDto>> getUserAddresses(@PathVariable("userId") Long userId);

    /**
     * 获取用户积分余额
     */
    @GetMapping("/points/{userId}")
    Result<Integer> getUserPoints(@PathVariable("userId") Long userId);

    /**
     * 扣减用户积分
     */
    @PostMapping("/points/deduct")
    Result<Boolean> deductPoints(@RequestParam("userId") Long userId, @RequestParam("points") Integer points);

    /**
     * 增加用户积分
     */
    @PostMapping("/points/add")
    Result<Boolean> addPoints(@RequestParam("userId") Long userId, @RequestParam("points") Integer points);

    /**
     * 获取用户钱包余额
     */
    @GetMapping("/wallet/{userId}")
    Result<java.math.BigDecimal> getUserWalletBalance(@PathVariable("userId") Long userId);

    /**
     * 扣减钱包余额
     */
    @PostMapping("/wallet/deduct")
    Result<Boolean> deductWalletBalance(@RequestParam("userId") Long userId, @RequestParam("amount") java.math.BigDecimal amount);
}

/**
 * 用户信息DTO
 */
class UserInfoDto {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    // getters and setters
}

/**
 * 地址信息DTO
 */
class AddressDto {
    private Long addressId;
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String postalCode;
    private Boolean isDefault;
    // getters and setters
}