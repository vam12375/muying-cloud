package com.muyingmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.order.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户地址Mapper接口
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 查询用户的所有地址
     * @param userId 用户ID
     * @return 地址列表
     */
    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    UserAddress selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 取消用户的所有默认地址
     * @param userId 用户ID
     * @return 影响行数
     */
    int cancelAllDefault(@Param("userId") Long userId);

    /**
     * 设置为默认地址
     * @param id 地址ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int setAsDefault(@Param("id") Long id, @Param("userId") Long userId);
}