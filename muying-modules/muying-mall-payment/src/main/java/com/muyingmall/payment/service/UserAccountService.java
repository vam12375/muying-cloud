package com.muyingmall.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muyingmall.payment.entity.UserAccount;
import java.math.BigDecimal;

public interface UserAccountService extends IService<UserAccount> {
    UserAccount findByUserId(Long userId);
    boolean updateBalance(Long userId, BigDecimal amount);
    boolean deductBalance(Long userId, BigDecimal amount);
    UserAccount createAccount(Long userId);
}