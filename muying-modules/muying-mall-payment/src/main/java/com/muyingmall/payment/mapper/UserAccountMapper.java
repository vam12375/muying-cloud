package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    
    @Select("SELECT * FROM user_accounts WHERE user_id = #{userId} AND account_type = 'WALLET' LIMIT 1")
    UserAccount findByUserId(Long userId);
    
    @Update("UPDATE user_accounts SET balance = balance + #{amount}, updated_at = NOW() WHERE user_id = #{userId}")
    int increaseBalance(Long userId, java.math.BigDecimal amount);
    
    @Update("UPDATE user_accounts SET balance = balance - #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND balance >= #{amount}")
    int decreaseBalance(Long userId, java.math.BigDecimal amount);
    
    @Update("UPDATE user_accounts SET frozen_amount = frozen_amount + #{amount}, updated_at = NOW() WHERE user_id = #{userId}")
    int freezeAmount(Long userId, java.math.BigDecimal amount);
    
    @Update("UPDATE user_accounts SET frozen_amount = frozen_amount - #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND frozen_amount >= #{amount}")
    int unfreezeAmount(Long userId, java.math.BigDecimal amount);
}