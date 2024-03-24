package io.github.maidsg.starter.start.component.aspect;

import io.github.maidsg.starter.start.annotation.PreventResubmit;
import io.github.maidsg.starter.start.component.RedissonLockAgent;
import io.github.maidsg.starter.start.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/*******************************************************************
 * <pre></pre>
 * @文件名称： PreventResubmitAspect.java
 * @包 路  径： io.github.maidsg.starter.start.aspect
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 11:18
 * @Modify：
 */
@Aspect
@Component
public class PreventResubmitAspect extends BaseAspect {

    @Resource
    private RedissonLockAgent redissonLockAgent;

    @Pointcut("@annotation(preventResubmit)")
    public void pointCut(PreventResubmit preventResubmit) {
    }

    @Around(value = "pointCut(preventResubmit)", argNames = "joinPoint,preventResubmit")
    public Object repeatSubmit(ProceedingJoinPoint joinPoint, PreventResubmit preventResubmit) throws Throwable {
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(resolveMethod(joinPoint));
        if (Objects.nonNull(preventResubmit)) {
            Object[] args = joinPoint.getArgs();
            String key = CollectionUtils.firstElement(getValueBySpEL(preventResubmit.lockKey(), parameterNames, args, "PreventResubmit"));
            boolean isLocked = false;
            try {
                isLocked = redissonLockAgent.fairLock(key, TimeUnit.SECONDS, preventResubmit.lockTime());
                // 如果成功获取到锁就继续执行
                if (isLocked) {
                    // 执行进程
                    return joinPoint.proceed();
                } else {
                    // 未获取到锁
                    throw new BusinessException("请勿重复提交");
                }
            } finally {

                // 如果锁还存在，在方法执行完成后，释放锁
                if (isLocked) {
                    redissonLockAgent.unlock(key);
                }
            }

        }
        return joinPoint.proceed();
    }

}
