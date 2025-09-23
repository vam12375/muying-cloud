package com.muyingmall.user.dto;

import com.muyingmall.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private User userInfo;
    private String token;
} 