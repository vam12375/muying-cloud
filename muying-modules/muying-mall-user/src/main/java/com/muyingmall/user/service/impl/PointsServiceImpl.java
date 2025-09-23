package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.user.entity.*;
import com.muyingmall.user.event.CheckinEvent;
import com.muyingmall.user.feign.OrderServiceFeignClient;
import com.muyingmall.user.feign.PointsServiceFeignClient;
import com.muyingmall.user.feign.dto.OrderDTO;
import com.muyingmall.user.feign.dto.PointsProductDTO;
import com.muyingmall.user.mapper.PointsHistoryMapper;
import com.muyingmall.user.mapper.PointsRuleMapper;
import com.muyingmall.user.service.MemberLevelService;
import com.muyingmall.user.service.PointsExchangeService;
import com.muyingmall.user.service.PointsOperationService;
import com.muyingmall.user.service.PointsService;
import com.muyingmall.common.enums.PointsOperationType;
import com.muyingmall.user.entity.UserPoints;
import com.muyingmall.user.mapper.UserPointsMapper;
import com.muyingmall.user.service.UserService;
import com.muyingmall.common.CacheConstants;
import com.muyingmall.common.util.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements PointsService {

    private final PointsHistoryMapper pointsHistoryMapper;
    private final PointsRuleMapper pointsRuleMapper;
    private final PointsOperationService pointsOperationService;
    private final MemberLevelService memberLevelService;
    private final UserPointsMapper userPointsMapper;
    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PointsServiceFeignClient pointsServiceFeignClient;
    private final OrderServiceFeignClient orderServiceFeignClient;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Override
    public Integer getUserPoints(Integer userId) {
        return pointsOperationService.getUserPoints(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPoints(Integer userId, Integer points, String source, String referenceId, String description) {
        return pointsOperationService.addPoints(userId, points, source, referenceId, description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductPoints(Integer userId, Integer points, String source, String referenceId, String description) {
        return pointsOperationService.deductPoints(userId, points, source, referenceId, description);
    }

    @Override
    public Page<PointsHistory> getUserPointsHistory(Integer userId, int page, int size) {
        Page<PointsHistory> pageParam = new Page<>(page, size);
        String cacheKey = CacheConstants.USER_POINTS_HISTORY_KEY + userId + ":page_" + page + ":size_" + size;
        Object cacheResult = null;
        if (redisUtil != null) {
            cacheResult = redisUtil.get(cacheKey);
            if (cacheResult != null) {
                return (Page<PointsHistory>) cacheResult;
            }
        }
        LambdaQueryWrapper<PointsHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsHistory::getUserId, userId).orderByDesc(PointsHistory::getCreateTime);
        Page<PointsHistory> result = pointsHistoryMapper.selectPage(pageParam, queryWrapper);
        if (result != null && !result.getRecords().isEmpty() && redisUtil != null) {
            redisUtil.set(cacheKey, result, CacheConstants.POINTS_HISTORY_EXPIRE_TIME);
        }
        return result;
    }

    @Override
    public List<PointsRule> getPointsRules() {
        LambdaQueryWrapper<PointsRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsRule::getEnabled, 1).orderByAsc(PointsRule::getSort);
        return pointsRuleMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer signIn(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        Map<String, Object> signInStatus = getSignInStatus(userId);
        if ((boolean) signInStatus.get("todaySigned")) {
            throw new BusinessException("您今日已签到，请明天再来");
        }
        PointsRule signInRule = pointsRuleMapper.selectOne(new LambdaQueryWrapper<PointsRule>().eq(PointsRule::getType, "signin").eq(PointsRule::getEnabled, 1));
        int signInPoints = (signInRule != null && signInRule.getValue() != null) ? signInRule.getValue() : 20;
        int continuousDays = (int) signInStatus.get("continuousDays");
        int newContinuousDays = continuousDays + 1;
        int extraPoints = 0;
        if (continuousDays >= 3) {
            extraPoints = (continuousDays - 2) * 5;
            if (continuousDays >= 7) {
                extraPoints = (continuousDays - 6) * 10 + 20;
            }
        }
        int totalPoints = signInPoints + extraPoints;
        String description = "每日签到";
        if (newContinuousDays == 3 || newContinuousDays == 7) {
            String continuousType = "signin_continuous_" + newContinuousDays;
            PointsRule continuousRule = pointsRuleMapper.selectOne(new LambdaQueryWrapper<PointsRule>().eq(PointsRule::getType, continuousType).eq(PointsRule::getEnabled, 1));
            if (continuousRule != null && continuousRule.getValue() != null) {
                int continuousPoints = continuousRule.getValue();
                this.addPoints(userId, continuousPoints, "signin_continuous", LocalDate.now().toString(), "连续签到" + newContinuousDays + "天额外奖励");
            }
        }
        if (extraPoints > 0) {
            description += "，连续签到" + newContinuousDays + "天";
        }
        if (!this.addPoints(userId, totalPoints, "signin", LocalDate.now().toString(), description)) {
            throw new BusinessException("签到积分记录失败");
        }
        return totalPoints;
    }

    @Override
    public Map<String, Object> getSignInStatus(Integer userId) {
        if (userId == null) throw new BusinessException("用户ID不能为空");
        Map<String, Object> result = new HashMap<>();
        String cacheKey = CacheConstants.USER_SIGNIN_STATUS_KEY + userId;
        Object cacheResult = null;
        if (redisUtil != null) {
            cacheResult = redisUtil.get(cacheKey);
            if (cacheResult != null) {
                Map<String, Object> cachedStatus = (Map<String, Object>) cacheResult;
                cachedStatus.put("totalPoints", getUserPoints(userId));
                cachedStatus.put("points", getUserPoints(userId));
                return cachedStatus;
            }
        }
        LocalDate today = LocalDate.now();
        boolean todaySigned = pointsHistoryMapper.selectCount(new LambdaQueryWrapper<PointsHistory>().eq(PointsHistory::getUserId, userId).eq(PointsHistory::getSource, "signin").between(PointsHistory::getCreateTime, today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusNanos(1))) > 0;
        int continuousDays = 0;
        LocalDate checkDate = today.minusDays(1);
        while (true) {
            if (pointsHistoryMapper.selectCount(new LambdaQueryWrapper<PointsHistory>().eq(PointsHistory::getUserId, userId).eq(PointsHistory::getSource, "signin").between(PointsHistory::getCreateTime, checkDate.atStartOfDay(), checkDate.plusDays(1).atStartOfDay().minusNanos(1))) > 0) {
                continuousDays++;
                checkDate = checkDate.minusDays(1);
            } else {
                break;
            }
        }
        Integer totalPoints = getUserPoints(userId);
        result.put("todaySigned", todaySigned);
        result.put("isSignedIn", todaySigned);
        result.put("continuousDays", continuousDays);
        result.put("historyMaxContinuousDays", continuousDays); // Simplified
        result.put("totalPoints", totalPoints);
        result.put("points", totalPoints);
        result.put("userLevel", memberLevelService.getLevelNameByPoints(totalPoints));
        if (redisUtil != null) {
            redisUtil.set(cacheKey, result, CacheConstants.POINTS_STATUS_EXPIRE_TIME);
        }
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchangeProduct(Integer userId, Long productId, Integer addressId, String phone) {
        PointsProductDTO product = pointsServiceFeignClient.getPointsProductById(productId);
        if (product == null) throw new BusinessException("商品不存在");
        if (product.getStatus() != 1) throw new BusinessException("商品已下架");
        if (product.getNeedAddress() == 1 && addressId == null) throw new BusinessException("请选择收货地址");
        if (product.getNeedPhone() == 1 && phone == null) throw new BusinessException("请输入手机号码");

        int totalPoints = product.getPoints();
        if (pointsOperationService.getUserPoints(userId) < totalPoints) throw new BusinessException("积分不足");

        PointsExchange exchange = new PointsExchange();
        exchange.setUserId(userId);
        exchange.setProductId(productId.intValue());
        exchange.setQuantity(1);
        exchange.setAddressId(addressId);
        exchange.setPhone(phone);
        exchange.setOrderNo("PE" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6));
        exchange.setPoints(totalPoints);
        exchange.setStatus(0); // 待发货
        exchange.setCreateTime(LocalDateTime.now());
        exchange.setUpdateTime(LocalDateTime.now());

        boolean success = pointsOperationService.deductPoints(userId, totalPoints, "exchange", exchange.getOrderNo(), "积分兑换商品");
        // Here should be another Feign call to update product stock in points service
        return success;
    }

    @Override
    public Page<Object> getPointsProducts(int page, int size, String category) {
        // This should be a Feign call to points service.
        log.warn("getPointsProducts is a placeholder and should be implemented via Feign client.");
        return new Page<>(page, size);
    }

    @Override
    public Object getPointsProductDetail(Long productId) {
        return pointsServiceFeignClient.getPointsProductById(productId);
    }

    @Override
    public Map<String, Object> userSignin(Integer userId) {
        if (userId == null) throw new BusinessException("用户ID不能为空");
        Map<String, Object> result = new HashMap<>();
        try {
            int earnedPoints = signIn(userId);
            redisUtil.del(CacheConstants.USER_SIGNIN_STATUS_KEY + userId);
            Map<String, Object> status = getSignInStatus(userId);
            result.putAll(status);
            result.put("earnedPoints", earnedPoints);
            result.put("success", true);
            result.put("message", "签到成功");
            applicationEventPublisher.publishEvent(new CheckinEvent(this, userId, earnedPoints, (int)status.get("continuousDays"), ""));
            return result;
        } catch (BusinessException be) {
            result.put("success", false);
            result.put("message", be.getMessage());
            result.putAll(getSignInStatus(userId));
            return result;
        }
    }

    @Override
    public boolean usePoints(Integer userId, int points, String source, String referenceId, String description) {
        return deductPoints(userId, points, source, referenceId, description);
    }

    @Override
    public Map<String, Object> getSignInCalendar(Integer userId, String month) {
        // Implementation is complex and depends on Redis, leaving as is for now.
        return new HashMap<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void awardPointsForOrder(Integer userId, Integer orderId, BigDecimal orderAmount) {
        if (userId == null || orderId == null || orderAmount == null) return;
        OrderDTO order = orderServiceFeignClient.getOrderById(orderId);
        if (order == null) {
            log.error("订单不存在, orderId={}", orderId);
            return;
        }
        if (order.getPointsUsed() != null && order.getPointsUsed() > 0) {
            log.info("订单 {} 使用了积分抵扣，不再奖励积分", orderId);
            return;
        }
        int pointsToAward = orderAmount.multiply(new BigDecimal("0.1")).intValue();
        if (pointsToAward > 0) {
            addPoints(userId, pointsToAward, "order_completed", orderId.toString(), "订单完成奖励");
        }
    }
    
    // Admin methods remain as placeholders or simplified
    @Override
    public Page<PointsHistory> adminListPointsHistory(Integer page, Integer size, Integer userId, String type, String source, LocalDate startDate, LocalDate endDate) {
        return new Page<>(page, size);
    }

    @Override
    public boolean adminAdjustPoints(Integer userId, Integer points, String description) {
        return points > 0 ? addPoints(userId, points, "admin", null, description) : deductPoints(userId, Math.abs(points), "admin", null, description);
    }

    @Override
    public Page<PointsExchange> adminListPointsExchanges(Integer page, Integer size, Integer userId, Long productId, String status, LocalDate startDate, LocalDate endDate) {
        return new Page<>(page, size);
    }

    @Override
    public boolean updateExchangeStatus(Long id, String status) {
        return true;
    }

    @Override
    public Map<String, Object> getPointsStats(LocalDate startDate, LocalDate endDate) {
        return new HashMap<>();
    }

    @Override
    public Page<UserPoints> pageWithUser(Page<UserPoints> page, LambdaQueryWrapper<UserPoints> queryWrapper) {
        this.page(page, queryWrapper);
        List<Long> userIds = page.getRecords().stream().map(UserPoints::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            List<User> users = userService.listByIds(userIds);
            Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));
            page.getRecords().forEach(up -> up.setUser(userMap.get(up.getUserId().intValue())));
        }
        return page;
    }
}