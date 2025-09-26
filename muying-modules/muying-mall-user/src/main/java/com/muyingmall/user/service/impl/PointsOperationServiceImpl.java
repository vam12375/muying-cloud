package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.core.constant.CacheConstants;
import com.muyingmall.common.core.exception.BusinessException;
import com.muyingmall.common.core.utils.RedisUtils;
import com.muyingmall.user.entity.PointsOperationLog;
import com.muyingmall.user.entity.UserPoints;
import com.muyingmall.user.mapper.PointsOperationLogMapper;
import com.muyingmall.user.mapper.UserPointsMapper;
import com.muyingmall.user.service.PointsOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 积分操作服务实现类
 * 负责处理用户积分的增减操作和操作日志记录
 */
@Service
@RequiredArgsConstructor
@Slf4j
public abstract class PointsOperationServiceImpl extends ServiceImpl<PointsOperationLogMapper, PointsOperationLog> implements PointsOperationService {

    private final UserPointsMapper userPointsMapper;
    private final PointsOperationLogMapper pointsOperationLogMapper;

    @Autowired(required = false)
    private RedisUtils redisUtils;

    @Override
    public Integer getUserPoints(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 尝试从缓存获取
        String cacheKey = CacheConstants.USER_POINTS_KEY + userId;
        if (redisUtils != null) {
            Object cachedPoints = redisUtils.get(cacheKey);
            if (cachedPoints != null) {
                return (Integer) cachedPoints;
            }
        }

        // 从数据库查询用户积分
        UserPoints userPoints = userPointsMapper.selectOne(
                new LambdaQueryWrapper<UserPoints>()
                        .eq(UserPoints::getUserId, userId)
        );

        Integer points = 0;
        if (userPoints != null) {
            points = userPoints.getCurrentPoints();
            if (points == null) {
                points = 0;
            }
        } else {
            // 如果用户积分记录不存在，创建一个
            userPoints = new UserPoints();
            userPoints.setUserId(userId);
            userPoints.setCurrentPoints(0);
            userPoints.setTotalEarned(0);
            userPoints.setTotalUsed(0);
            userPoints.setCreateTime(LocalDateTime.now());
            userPoints.setUpdateTime(LocalDateTime.now());
            userPointsMapper.insert(userPoints);
        }

        // 更新缓存
        if (redisUtils != null) {
            redisUtils.set(cacheKey, points, CacheConstants.POINTS_CACHE_EXPIRE_TIME);
        }

        return points;
    }

    @Override
    public Integer getTotalEarnedPoints(Integer userId) {
        if (userId == null) {
            return 0;
        }

        UserPoints userPoints = userPointsMapper.selectOne(
                new LambdaQueryWrapper<UserPoints>()
                        .eq(UserPoints::getUserId, userId)
        );

        return userPoints != null && userPoints.getTotalEarned() != null ? 
               userPoints.getTotalEarned() : 0;
    }

    @Override
    public Integer getTotalUsedPoints(Integer userId) {
        if (userId == null) {
            return 0;
        }

        UserPoints userPoints = userPointsMapper.selectOne(
                new LambdaQueryWrapper<UserPoints>()
                        .eq(UserPoints::getUserId, userId)
        );

        return userPoints != null && userPoints.getTotalUsed() != null ? 
               userPoints.getTotalUsed() : 0;
    }

    @Override
    @Transactional
    public boolean addPoints(Integer userId, Integer points, Integer operationType, String description) {
        if (userId == null || points == null || points <= 0) {
            throw new BusinessException("参数错误：用户ID和积分数量不能为空且积分必须大于0");
        }

        try {
            // 更新用户积分
            UserPoints userPoints = userPointsMapper.selectOne(
                    new LambdaQueryWrapper<UserPoints>()
                            .eq(UserPoints::getUserId, userId)
            );

            if (userPoints == null) {
                // 创建新的积分记录
                userPoints = new UserPoints();
                userPoints.setUserId(userId);
                userPoints.setCurrentPoints(points);
                userPoints.setTotalEarned(points);
                userPoints.setTotalUsed(0);
                userPoints.setCreateTime(LocalDateTime.now());
                userPoints.setUpdateTime(LocalDateTime.now());
                userPointsMapper.insert(userPoints);
            } else {
                // 更新现有积分记录
                userPoints.setCurrentPoints(userPoints.getCurrentPoints() + points);
                userPoints.setTotalEarned(userPoints.getTotalEarned() + points);
                userPoints.setUpdateTime(LocalDateTime.now());
                userPointsMapper.updateById(userPoints);
            }

            // 记录积分操作日志
            PointsOperationLog log = new PointsOperationLog();
            log.setUserId(userId);
            log.setOperationType(operationType);
            log.setPoints(points);
            log.setDescription(description);
            log.setBalanceAfter(userPoints.getCurrentPoints());
            log.setCreateTime(LocalDateTime.now());
            pointsOperationLogMapper.insert(log);

            // 清除缓存
            clearPointsCache(userId);

            log.info("用户积分增加成功: userId={}, points={}, newBalance={}", 
                    userId, points, userPoints.getCurrentPoints());

            return true;

        } catch (Exception e) {
            log.error("用户积分增加失败: userId={}, points={}", userId, points, e);
            throw new BusinessException("积分增加失败");
        }
    }

    @Override
    @Transactional
    public boolean deductPoints(Integer userId, Integer points, Integer operationType, String description) {
        if (userId == null || points == null || points <= 0) {
            throw new BusinessException("参数错误：用户ID和积分数量不能为空且积分必须大于0");
        }

        try {
            // 查询用户当前积分
            UserPoints userPoints = userPointsMapper.selectOne(
                    new LambdaQueryWrapper<UserPoints>()
                            .eq(UserPoints::getUserId, userId)
            );

            if (userPoints == null || userPoints.getCurrentPoints() < points) {
                throw new BusinessException("用户积分不足");
            }

            // 更新积分
            userPoints.setCurrentPoints(userPoints.getCurrentPoints() - points);
            userPoints.setTotalUsed(userPoints.getTotalUsed() + points);
            userPoints.setUpdateTime(LocalDateTime.now());
            userPointsMapper.updateById(userPoints);

            // 记录积分操作日志
            PointsOperationLog log = new PointsOperationLog();
            log.setUserId(userId);
            log.setOperationType(operationType);
            log.setPoints(-points); // 负数表示扣减
            log.setDescription(description);
            log.setBalanceAfter(userPoints.getCurrentPoints());
            log.setCreateTime(LocalDateTime.now());
            pointsOperationLogMapper.insert(log);

            // 清除缓存
            clearPointsCache(userId);

            log.info("用户积分扣减成功: userId={}, points={}, newBalance={}", 
                    userId, points, userPoints.getCurrentPoints());

            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户积分扣减失败: userId={}, points={}", userId, points, e);
            throw new BusinessException("积分扣减失败");
        }
    }

    @Override
    public Page<PointsOperationLog> getPointsHistory(Integer userId, Integer page, Integer size, Integer operationType) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        LambdaQueryWrapper<PointsOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointsOperationLog::getUserId, userId);
        
        if (operationType != null) {
            queryWrapper.eq(PointsOperationLog::getOperationType, operationType);
        }
        
        queryWrapper.orderByDesc(PointsOperationLog::getCreateTime);

        Page<PointsOperationLog> pageParam = new Page<>(
                page != null ? page : 1, 
                size != null ? size : 10
        );

        return pointsOperationLogMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 清除用户积分相关缓存
     */
    private void clearPointsCache(Integer userId) {
        if (redisUtils != null && userId != null) {
            try {
                // 清除用户积分缓存
                redisUtils.del(CacheConstants.USER_POINTS_KEY + userId);
                // 清除签到状态缓存
                redisUtils.del(CacheConstants.USER_SIGNIN_STATUS_KEY + userId);
                // 清除积分历史缓存（模糊删除）
                String historyPattern = CacheConstants.USER_POINTS_HISTORY_KEY + userId + ":*";
                redisUtils.delPattern(historyPattern);
            } catch (Exception e) {
                log.warn("清除用户 {} 积分缓存失败：{}", userId, e.getMessage());
            }
        }
    }
}