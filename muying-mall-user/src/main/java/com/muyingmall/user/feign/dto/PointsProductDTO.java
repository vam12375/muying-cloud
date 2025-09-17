package com.muyingmall.user.feign.dto;

import lombok.Data;

@Data
public class PointsProductDTO {
    private Long id;
    private String name;
    private Integer points;
    private Integer stock;
    private Integer status;
    private Integer needAddress;
    private Integer needPhone;
}
