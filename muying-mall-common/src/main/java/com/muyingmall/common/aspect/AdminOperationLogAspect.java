package com.muyingmall.common.aspect;

import com.muyingmall.common.annotation.AdminOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 管理员操作日志切面
 * 只有在配置文件中启用时才生效
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "muying-mall.admin.operation-log.enabled", havingValue = "true", matchIfMissing = false)
public class AdminOperationLogAspect {

    @Pointcut("@annotation(com.muyingmall.common.annotation.AdminOperationLog)")
    public void operationLogPointcut() {}

    @Around("operationLogPointcut() && @annotation(adminOperationLog)")
    public Object around(ProceedingJoinPoint joinPoint, AdminOperationLog adminOperationLog) throws Throwable {
        log.info("管理员操作日志切面暂未完整实现，需要相关服务支持");
        return joinPoint.proceed();
    }
}