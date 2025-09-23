package com.muyingmall.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muyingmall.payment.entity.User;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    User findByEmail(String email);
    User createUser(String username, String email);
}