package com.muyingmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muyingmall.common.CacheConstants;
import com.muyingmall.common.exception.BusinessException;
import com.muyingmall.common.util.RedisUtil;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 积分操作服务实现类
 * 负责处理用户积分的增减操作和操作日志记录
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PointsOperationServiceImpl extends ServiceImpl<PointsOperationLogMapper, PointsOperationLog> implements PointsOperationService {

    private final UserPointsMapper userPointsMapper;
    private final PointsOperationLogMapper pointsOperationLogMapper;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Override
    public Integer getUserPoints(Integer userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 尝试从缓存获取
        String cacheKey = CacheConstants.USER_POINTS_KEY + userId;
        if (redisUtil != null) {
            Object cachedPoints = redisUtil.get(cacheKey);
            if (cachedPoints != null) {
                return (Integer) cachedPoints;
            }
        }

        // 从数据库查询
        LambdaQueryWrapper<UserPoints> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPoints::getUserId, userId.longValue());
        UserPoints userPoints = userPointsMapper.selectOne(queryWrapper);

        Integer points = 0;
        if (userPoints != null) {
            points = userPoints.getPoints() != null ? userPoints.getPoints() : 0;
        } else {
            // 如果用户积分记录不存在，创建一个初始记录
            userPoints = new UserPoints();
            userPoints.setUserId(userId.longValue());
            userPoints.setPoints(0);
            userPoints.setLevel("Bronze");
            userPoints.setCreateTime(LocalDateTime.now());
            userPoints.setUpdateTime(LocalDateTime.now());
            userPointsMapper.insert(userPoints);
        }

        // 更新缓存
        if (redisUtil != null) {
            redisUtil.set(cacheKey, points, CacheConstants.POINTS_CACHE_EXPIRE_TIME);
        }

        return points;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPoints(Integer userId, Integer points, String source, String referenceId, String description) {
        if (userId == null || points == null || points <= 0) {
            throw new BusinessException("参数错误：用户ID和积分数量不能为空，积分数量必须大于0");
        }

        try {
            // 获取当前积分
            Integer currentPoints = getUserPoints(userId);
            Integer newPoints = currentPoints + points;

            // 更新用户积分
            LambdaUpdateWrapper<UserPoints> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserPoints::getUserId, userId.longValue())
                    .set(UserPoints::getPoints, newPoints)
                    .set(UserPoints::getUpdateTime, LocalDateTime.now());

            int updateResult = userPointsMapper.update(null, updateWrapper);
            if (updateResult <= 0) {
                throw new BusinessException("积分更新失败");
            }

            // 记录操作日志
            boolean logResult = recordOperation(userId, "EARN", points, newPoints, description,
                    referenceId != null ? Integer.valueOf(referenceId) : null);
            if (!logResult) {
                throw new BusinessException("积分操作日志记录失败");
            }

            // 清除缓存
            clearPointsCache(userId);

            log.info("用户 {} 积分增加成功：+{} 积分，当前总积分：{}", userId, points, newPoints);
            return true;

        } catch (Exception e) {
            log.error("用户 {} 积分增加失败：{}", userId, e.getMessage(), e);
            throw new BusinessException("积分增加操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductPoints(Integer userId, Integer points, String source, String referenceId, String description) {
        if (userId == null || points == null || points <= 0) {
            throw new BusinessException("参数错误：用户ID和积分数量不能为空，积分数量必须大于0");
        }

        try {
            // 获取当前积分
            Integer currentPoints = getUserPoints(userId);
            if (currentPoints < points) {
                throw new BusinessException("积分余额不足，当前积分：" + currentPoints + "，需要扣除：" + points);
            }

            Integer newPoints = currentPoints - points;

            // 更新用户积分
            LambdaUpdateWrapper<UserPoints> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserPoints::getUserId, userId.longValue())
                    .set(UserPoints::getPoints, newPoints)
                    .set(UserPoints::getUpdateTime, LocalDateTime.now());

            int updateResult = userPointsMapper.update(null, updateWrapper);
            if (updateResult <= 0) {
                throw new BusinessException("积分更新失败");
            }

            // 记录操作日志
            boolean logResult = recordOperation(userId, "SPEND", -points, newPoints, description,
                    referenceId != null ? Integer.valueOf(referenceId) : null);
            if (!logResult) {
                throw new BusinessException("积分操作日志记录失败");
            }

            // 清除缓存
            clearPointsCache(userId);

            log.info("用户 {} 积分扣除成功：-{} 积分，当前总积分：{}", userId, points, newPoints);
            return true;

        } catch (Exception e) {
            log.error("用户 {} 积分扣除失败：{}", userId, e.getMessage(), e);
            throw new BusinessException("积分扣除操作失败：" + e.getMessage());
        }
    }

    @Override
    public boolean recordOperation(Integer userId, String operationType, Integer pointsChange,
                                 Integer currentBalance, String description, Integer relatedOrderId) {
        try {
            PointsOperationLog operationLog = new PointsOperationLog();
            operationLog.setUserId(userId);
            operationLog.setOperationType(operationType);
            operationLog.setPointsChange(pointsChange);
            operationLog.setCurrentBalance(currentBalance);
            operationLog.setDescription(description);
            operationLog.setRelatedOrderId(relatedOrderId);
            operationLog.setCreateTime(LocalDateTime.now());

            int result = pointsOperationLogMapper.insert(operationLog);
            return result > 0;

        } catch (Exception e) {
            log.error("记录积分操作日志失败：userId={}, operationType={}, pointsChange={}, error={}",
                    userId, operationType, pointsChange, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Page<PointsOperationLog> adminListOperationLogs(Integer page, Integer size, Integer userId,
                                                         String operationType, LocalDate startDate, LocalDate endDate) {
        Page<PointsOperationLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PointsOperationLog> queryWrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            queryWrapper.eq(PointsOperationLog::getUserId, userId);
        }
        if (operationType != null && !operationType.trim().isEmpty()) {
            queryWrapper.eq(PointsOperationLog::getOperationType, operationType);
        }
        if (startDate != null) {
            queryWrapper.ge(PointsOperationLog::getCreateTime, startDate.atStartOfDay());
        }
        if (endDate != null) {
            queryWrapper.le(PointsOperationLog::getCreateTime, endDate.plusDays(1).atStartOfDay());
        }

        queryWrapper.orderByDesc(PointsOperationLog::getCreateTime);

        return pointsOperationLogMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 清除用户积分相关缓存
     */
    private void clearPointsCache(Integer userId) {
        if (redisUtil != null && userId != null) {
            try {
                // 清除用户积分缓存
                redisUtil.del(CacheConstants.USER_POINTS_KEY + userId);
                // 清除签到状态缓存
                redisUtil.del(CacheConstants.USER_SIGNIN_STATUS_KEY + userId);
                // 清除积分历史缓存（模糊删除）
                String historyPattern = CacheConstants.USER_POINTS_HISTORY_KEY + userId + ":*";
                redisUtil.delPattern(historyPattern);
            } catch (Exception e) {
                log.warn("清除用户 {} 积分缓存失败：{}", userId, e.getMessage());
            }
        }
    }
}