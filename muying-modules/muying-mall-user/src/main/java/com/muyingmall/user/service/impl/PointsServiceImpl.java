package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.user.dto.PointsHistory;
import com.muyingmall.user.entity.PointsOperationLog;
import com.muyingmall.user.entity.User;
import com.muyingmall.user.entity.UserPoints;
import com.muyingmall.user.mapper.PointsOperationLogMapper;
import com.muyingmall.user.mapper.UserMapper;
import com.muyingmall.user.mapper.UserPointsMapper;
import com.muyingmall.user.service.MemberLevelService;
import com.muyingmall.user.service.PointsOperationService;
import com.muyingmall.user.service.PointsService;
import com.muyingmall.common.core.enums.PointsOperationType;
import com.muyingmall.common.core.constant.CacheConstants;
import com.muyingmall.common.core.utils.RedisUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分服务实现类
 * 负责用户积分的查询、签到、历史记录等功能
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements PointsService {

    private final UserPointsMapper userPointsMapper;
    private final PointsOperationLogMapper pointsHistoryMapper;
    private final UserMapper userMapper;
    private final PointsOperationService pointsOperationService;
    private final MemberLevelService memberLevelService;

    @Autowired(required = false)
    private RedisUtils redisUtils;

    @Override
    public Integer getUserPoints(Integer userId) {
        if (userId == null) {
            return 0;
        }

        return pointsOperationService.getUserPoints(userId);
    }

    @Override
    public Page<PointsHistory> getPointsHistory(Integer userId, Integer page, Integer size, Integer type) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 构建缓存键
        String cacheKey = String.format("%s%d:%d:%d:%s", 
                CacheConstants.USER_POINTS_HISTORY_KEY, userId, 
                page != null ? page : 1, 
                size != null ? size : 10,
                type != null ? type : "all");

        // 先从缓存中查找
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                return (Page<PointsHistory>) cacheResult;
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<PointsOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsOperationLog::getUserId, userId);
        
        if (type != null) {
            queryWrapper.eq(PointsOperationLog::getOperationType, type);
        }
        
        queryWrapper.orderByDesc(PointsOperationLog::getCreateTime);

        // 分页查询
        Page<PointsOperationLog> pageParam = new Page<>(
                page != null ? page : 1, 
                size != null ? size : 10
        );
        
        Page<PointsOperationLog> result = pointsHistoryMapper.selectPage(pageParam, queryWrapper);
        if (result != null && !result.getRecords().isEmpty() && redisUtils != null) {
            redisUtils.set(cacheKey, result, CacheConstants.POINTS_HISTORY_EXPIRE_TIME);
        }
        return result;
    }

    @Override
    public Map<String, Object> getPointsStatus(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 构建缓存键
        String cacheKey = CacheConstants.USER_POINTS_STATUS_KEY + userId;

        // 先从缓存中查找
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                Map<String, Object> cachedStatus = (Map<String, Object>) cacheResult;
                // 更新签到状态（实时查询）
                boolean isSignedToday = isSignedToday(userId);
                cachedStatus.put("isSignedToday", isSignedToday);
                return cachedStatus;
            }
        }

        // 从数据库查询
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前积分
        Integer currentPoints = getUserPoints(userId);
        result.put("currentPoints", currentPoints);
        
        // 获取总积分（历史累计）
        Integer totalPoints = pointsOperationService.getTotalEarnedPoints(userId);
        result.put("totalPoints", totalPoints);
        
        // 获取今日是否已签到
        boolean isSignedToday = isSignedToday(userId);
        result.put("isSignedToday", isSignedToday);
        
        // 获取连续签到天数
        Integer consecutiveDays = getConsecutiveSignInDays(userId);
        result.put("consecutiveDays", consecutiveDays);
        
        // 获取用户等级
        result.put("userLevel", memberLevelService.getLevelNameByPoints(totalPoints));
        if (redisUtils != null) {
            redisUtils.set(cacheKey, result, CacheConstants.POINTS_STATUS_EXPIRE_TIME);
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> signIn(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 检查今日是否已签到
        if (isSignedToday(userId)) {
            throw new BusinessException("今日已签到，请勿重复签到");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        Map<String, Object> result = new HashMap<>();
        
        try {
            int earnedPoints = doSignIn(userId);
            if (redisUtils != null) {
                redisUtils.del(CacheConstants.USER_SIGNIN_STATUS_KEY + userId);
            }
            Map<String, Object> status = getSignInStatus(userId);
            result.putAll(status);
            result.put("earnedPoints", earnedPoints);
            result.put("message", "签到成功！获得 " + earnedPoints + " 积分");
            
            log.info("用户签到成功: userId={}, earnedPoints={}", userId, earnedPoints);
            
        } catch (Exception e) {
            log.error("用户签到失败: userId={}", userId, e);
            throw new BusinessException("签到失败，请重试");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getSignInStatus(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 构建缓存键
        String cacheKey = CacheConstants.USER_SIGNIN_STATUS_KEY + userId;

        // 先从缓存中查找
        Object cacheResult = null;
        if (redisUtils != null) {
            cacheResult = redisUtils.get(cacheKey);
            if (cacheResult != null) {
                return (Map<String, Object>) cacheResult;
            }
        }

        Map<String, Object> result = new HashMap<>();
        
        // 获取今日是否已签到
        boolean isSignedToday = isSignedToday(userId);
        result.put("isSignedToday", isSignedToday);
        
        // 获取连续签到天数
        Integer consecutiveDays = getConsecutiveSignInDays(userId);
        result.put("consecutiveDays", consecutiveDays);
        
        // 计算签到可获得的积分
        int signInPoints = calculateSignInPoints(consecutiveDays);
        result.put("signInPoints", signInPoints);
        
        // 获取本月签到天数
        Integer monthlySignInDays = getMonthlySignInDays(userId);
        result.put("monthlySignInDays", monthlySignInDays);

        // 缓存结果
        if (redisUtils != null) {
            redisUtils.set(cacheKey, result, CacheConstants.SIGNIN_STATUS_EXPIRE_TIME);
        }
        
        return result;
    }

    @Override
    public boolean isSignedToday(Integer userId) {
        if (userId == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        
        Long count = pointsHistoryMapper.selectCount(
                new LambdaQueryWrapper<PointsOperationLog>()
                        .eq(PointsOperationLog::getUserId, userId)
                        .eq(PointsOperationLog::getOperationType, PointsOperationType.SIGN_IN.getCode())
                        .ge(PointsOperationLog::getCreateTime, today.atStartOfDay())
                        .lt(PointsOperationLog::getCreateTime, today.plusDays(1).atStartOfDay())
        );
        
        return count != null && count > 0;
    }

    @Override
    public Integer getConsecutiveSignInDays(Integer userId) {
        if (userId == null) {
            return 0;
        }

        // 查询最近的签到记录
        List<PointsOperationLog> recentSignIns = pointsHistoryMapper.selectList(
                new LambdaQueryWrapper<PointsOperationLog>()
                        .eq(PointsOperationLog::getUserId, userId)
                        .eq(PointsOperationLog::getOperationType, PointsOperationType.SIGN_IN.getCode())
                        .orderByDesc(PointsOperationLog::getCreateTime)
                        .last("LIMIT 30") // 最多查询30天
        );

        if (recentSignIns.isEmpty()) {
            return 0;
        }

        // 计算连续签到天数
        int consecutiveDays = 0;
        LocalDate currentDate = LocalDate.now();
        
        // 如果今天没有签到，从昨天开始计算
        if (!isSignedToday(userId)) {
            currentDate = currentDate.minusDays(1);
        }

        for (PointsOperationLog signIn : recentSignIns) {
            LocalDate signInDate = signIn.getCreateTime().toLocalDate();
            
            if (signInDate.equals(currentDate)) {
                consecutiveDays++;
                currentDate = currentDate.minusDays(1);
            } else {
                break;
            }
        }

        return consecutiveDays;
    }

    @Override
    public Integer getMonthlySignInDays(Integer userId) {
        if (userId == null) {
            return 0;
        }

        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());

        Long count = pointsHistoryMapper.selectCount(
                new LambdaQueryWrapper<PointsOperationLog>()
                        .eq(PointsOperationLog::getUserId, userId)
                        .eq(PointsOperationLog::getOperationType, PointsOperationType.SIGN_IN.getCode())
                        .ge(PointsOperationLog::getCreateTime, monthStart.atStartOfDay())
                        .le(PointsOperationLog::getCreateTime, monthEnd.atTime(23, 59, 59))
        );

        return count != null ? count.intValue() : 0;
    }

    /**
     * 执行签到操作
     */
    private int doSignIn(Integer userId) {
        // 计算签到积分
        Integer consecutiveDays = getConsecutiveSignInDays(userId);
        int points = calculateSignInPoints(consecutiveDays);
        
        // 添加积分
        pointsOperationService.addPoints(userId, points, PointsOperationType.SIGN_IN.getCode(), "每日签到");
        
        return points;
    }

    /**
     * 计算签到积分
     */
    private int calculateSignInPoints(Integer consecutiveDays) {
        if (consecutiveDays == null) {
            consecutiveDays = 0;
        }
        
        // 基础签到积分
        int basePoints = 10;
        
        // 连续签到奖励
        if (consecutiveDays >= 30) {
            return basePoints + 20; // 连续30天额外20积分
        } else if (consecutiveDays >= 15) {
            return basePoints + 15; // 连续15天额外15积分
        } else if (consecutiveDays >= 7) {
            return basePoints + 10; // 连续7天额外10积分
        } else if (consecutiveDays >= 3) {
            return basePoints + 5;  // 连续3天额外5积分
        }
        
        return basePoints;
    }
}