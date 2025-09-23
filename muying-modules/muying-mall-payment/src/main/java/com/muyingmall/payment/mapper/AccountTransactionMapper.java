package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.AccountTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountTransactionMapper extends BaseMapper<AccountTransaction> {
    
    @Select("SELECT * FROM account_transactions WHERE account_id = #{accountId} ORDER BY created_at DESC")
    List<AccountTransaction> findByAccountId(Long accountId);
    
    @Select("SELECT * FROM account_transactions WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<AccountTransaction> findByUserId(Long userId);
    
    @Select("SELECT * FROM account_transactions WHERE reference_id = #{referenceId}")
    AccountTransaction findByReferenceId(String referenceId);
}