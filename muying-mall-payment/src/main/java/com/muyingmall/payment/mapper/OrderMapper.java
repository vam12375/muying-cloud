package com.muyingmall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muyingmall.payment.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    @Select("SELECT * FROM orders WHERE order_number = #{orderNumber}")
    Order findByOrderNumber(String orderNumber);
    
    @Update("UPDATE orders SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(Long id, String status);
    
    @Update("UPDATE orders SET payment_time = NOW(), updated_at = NOW() WHERE id = #{id}")
    int updatePaymentTime(Long id);
}