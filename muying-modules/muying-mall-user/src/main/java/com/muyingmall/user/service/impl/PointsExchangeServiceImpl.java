package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.user.entity.PointsExchange;
import com.muyingmall.user.mapper.PointsExchangeMapper;
import com.muyingmall.user.service.PointsExchangeService;
import com.muyingmall.user.service.PointsOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 积分兑换服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PointsExchangeServiceImpl extends ServiceImpl<PointsExchangeMapper, PointsExchange> implements PointsExchangeService {

    private final PointsOperationService pointsOperationService;
    
    // 用于生成订单号的序列号
    private static final AtomicLong ORDER_SEQUENCE = new AtomicLong(1);
    
    // 兑换状态常量
    private static final int STATUS_PENDING = 0;  // 待发货
    private static final int STATUS_SHIPPED = 1;  // 已发货
    private static final int STATUS_COMPLETED = 2; // 已完成
    private static final int STATUS_CANCELLED = 3; // 已取消

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointsExchange createExchange(PointsExchange exchange) {
        if (exchange == null || exchange.getUserId() == null || exchange.getProductId() == null 
            || exchange.getPoints() == null || exchange.getPoints() <= 0) {
            throw new BusinessException("兑换参数不能为空");
        }

        // 设置默认数量
        if (exchange.getQuantity() == null || exchange.getQuantity() <= 0) {
            exchange.setQuantity(1);
        }

        // 检查用户积分是否足够
        Integer userPoints = pointsOperationService.getUserPoints(exchange.getUserId());
        if (userPoints < exchange.getPoints()) {
            throw new BusinessException("积分余额不足，当前积分：" + userPoints + "，需要积分：" + exchange.getPoints());
        }

        try {
            // 扣减用户积分
            boolean deductResult = pointsOperationService.deductPoints(
                exchange.getUserId(),
                exchange.getPoints(),
                "EXCHANGE",
                null,
                "积分兑换商品，商品ID：" + exchange.getProductId()
            );

            if (!deductResult) {
                throw new BusinessException("积分扣减失败");
            }

            // 生成兑换订单号
            String orderNo = generateOrderNo();
            
            // 设置兑换信息
            exchange.setOrderNo(orderNo);
            exchange.setStatus(STATUS_PENDING);
            exchange.setCreateTime(LocalDateTime.now());
            exchange.setUpdateTime(LocalDateTime.now());

            // 保存兑换记录
            boolean saveResult = save(exchange);
            if (!saveResult) {
                throw new BusinessException("兑换订单创建失败");
            }

            log.info("用户 {} 创建积分兑换订单成功，订单号：{}，消耗积分：{}", 
                    exchange.getUserId(), orderNo, exchange.getPoints());

            return exchange;

        } catch (Exception e) {
            log.error("用户 {} 创建积分兑换订单失败：{}", exchange.getUserId(), e.getMessage(), e);
            throw new BusinessException("兑换失败：" + e.getMessage());
        }
    }

    @Override
    public Page<PointsExchange> getUserExchanges(Integer userId, int page, int size, Integer status) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        Page<PointsExchange> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PointsExchange> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(PointsExchange::getUserId, userId);
        
        if (status != null) {
            queryWrapper.eq(PointsExchange::getStatus, status);
        }
        
        queryWrapper.orderByDesc(PointsExchange::getCreateTime);

        return page(pageParam, queryWrapper);
    }

    @Override
    public PointsExchange getExchangeDetail(Long id) {
        if (id == null) {
            throw new BusinessException("兑换ID不能为空");
        }

        PointsExchange exchange = getById(id);
        if (exchange == null) {
            throw new BusinessException("兑换记录不存在");
        }

        return exchange;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ship(Long id, String trackingNo, String trackingCompany) {
        if (id == null) {
            throw new BusinessException("兑换ID不能为空");
        }

        PointsExchange exchange = getById(id);
        if (exchange == null) {
            throw new BusinessException("兑换记录不存在");
        }

        if (exchange.getStatus() != STATUS_PENDING) {
            throw new BusinessException("只有待发货状态的订单才能发货");
        }

        try {
            LambdaUpdateWrapper<PointsExchange> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PointsExchange::getId, id)
                    .set(PointsExchange::getStatus, STATUS_SHIPPED)
                    .set(PointsExchange::getTrackingNo, trackingNo)
                    .set(PointsExchange::getTrackingCompany, trackingCompany)
                    .set(PointsExchange::getUpdateTime, LocalDateTime.now());

            boolean updateResult = update(updateWrapper);
            if (updateResult) {
                log.info("兑换订单 {} 发货成功，物流单号：{}", id, trackingNo);
            }

            return updateResult;

        } catch (Exception e) {
            log.error("兑换订单 {} 发货失败：{}", id, e.getMessage(), e);
            throw new BusinessException("发货失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean complete(Long id) {
        if (id == null) {
            throw new BusinessException("兑换ID不能为空");
        }

        PointsExchange exchange = getById(id);
        if (exchange == null) {
            throw new BusinessException("兑换记录不存在");
        }

        if (exchange.getStatus() != STATUS_SHIPPED) {
            throw new BusinessException("只有已发货状态的订单才能完成");
        }

        try {
            LambdaUpdateWrapper<PointsExchange> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PointsExchange::getId, id)
                    .set(PointsExchange::getStatus, STATUS_COMPLETED)
                    .set(PointsExchange::getUpdateTime, LocalDateTime.now());

            boolean updateResult = update(updateWrapper);
            if (updateResult) {
                log.info("兑换订单 {} 完成", id);
            }

            return updateResult;

        } catch (Exception e) {
            log.error("兑换订单 {} 完成失败：{}", id, e.getMessage(), e);
            throw new BusinessException("完成订单失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long id, String reason) {
        if (id == null) {
            throw new BusinessException("兑换ID不能为空");
        }

        PointsExchange exchange = getById(id);
        if (exchange == null) {
            throw new BusinessException("兑换记录不存在");
        }

        if (exchange.getStatus() == STATUS_COMPLETED || exchange.getStatus() == STATUS_CANCELLED) {
            throw new BusinessException("已完成或已取消的订单不能取消");
        }

        try {
            // 如果订单还未发货，需要返还积分
            if (exchange.getStatus() == STATUS_PENDING) {
                boolean refundResult = pointsOperationService.addPoints(
                    exchange.getUserId(),
                    exchange.getPoints(),
                    "EXCHANGE_CANCEL",
                    exchange.getId().toString(),
                    "取消积分兑换订单，返还积分：" + reason
                );

                if (!refundResult) {
                    throw new BusinessException("积分返还失败");
                }
            }

            // 更新订单状态
            LambdaUpdateWrapper<PointsExchange> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PointsExchange::getId, id)
                    .set(PointsExchange::getStatus, STATUS_CANCELLED)
                    .set(PointsExchange::getRemark, reason)
                    .set(PointsExchange::getUpdateTime, LocalDateTime.now());

            boolean updateResult = update(updateWrapper);
            if (updateResult) {
                log.info("兑换订单 {} 取消成功，原因：{}", id, reason);
            }

            return updateResult;

        } catch (Exception e) {
            log.error("兑换订单 {} 取消失败：{}", id, e.getMessage(), e);
            throw new BusinessException("取消订单失败：" + e.getMessage());
        }
    }

    /**
     * 生成兑换订单号
     * 格式：PE + yyyyMMddHHmmss + 4位序列号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long sequence = ORDER_SEQUENCE.getAndIncrement();
        if (sequence > 9999) {
            ORDER_SEQUENCE.set(1);
            sequence = 1;
        }
        return String.format("PE%s%04d", timestamp, sequence);
    }
}