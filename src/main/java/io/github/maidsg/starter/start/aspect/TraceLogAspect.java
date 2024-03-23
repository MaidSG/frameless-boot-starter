package io.github.maidsg.starter.start.aspect;

import io.github.maidsg.starter.start.annotation.Traceable;
import io.github.maidsg.starter.start.dao.LogFileDataSource;
import io.github.maidsg.starter.start.enums.LogLevelEnum;
import io.github.maidsg.starter.start.interfaces.LogBackDataSource;
import io.github.maidsg.starter.start.model.base.LogInfo;
import io.github.maidsg.starter.start.model.base.Res;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.hutool.core.exception.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/*******************************************************************
 * <pre></pre>
 * @文件名称： TraceLogAspect.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.aspect
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 17:57
 * @Modify：
 */

@Component
@Aspect
@Slf4j
@EnableAsync
public class TraceLogAspect extends BaseAspect{

    @Autowired
    private BootStarterProperties.LogBackProperties logBackProperties;

    private long currentTime = 0L;

    @Pointcut("@annotation(io.github.maidsg.starter.start.annotation.Traceable)")
    public void logPointcut() {
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        currentTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        LogInfo logBean = getResponseLogInfo(result);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (result != null) {
            logBean.setPath(request.getRequestURI());
        }
        saveLog(joinPoint, logBean);
        return result;
    }


    private LogInfo getResponseLogInfo(Object result) {
        //判断返回结果
        LogInfo logBean = new LogInfo(LogLevelEnum.INFO.name(), System.currentTimeMillis() - currentTime);

        if (result instanceof Res) {
            Res res = (Res) result;
            if (!res.getSuccess() || res.getCode() != Res.SUCCESS_CODE) {
                logBean.setLogType(LogLevelEnum.ERROR.name());
                logBean.setExceptionDetail(res.getMsg());
            } else {
                logBean.setResult(res.getData());
                logBean.setLogType(LogLevelEnum.INFO.name());
            }
            logBean.setRequestId(res.getRequestId());
        }
        return logBean;
    }


    private void saveLog(JoinPoint joinPoint, LogInfo logBean) {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Traceable traceable = method.getAnnotation(Traceable.class);
            String methodName = method.getName();
            //注解入参
            logBean.setDescription(traceable.desc());
            logBean.setPlatformType(traceable.platformType().getDesc());
            //方法全路径
            logBean.setMethod(joinPoint.getTarget().getClass().getName() + "." + methodName + "()");
            if (null != logBackProperties && null !=  logBackProperties.getFileDataSource()) {
                Class<?> clazz = Class.forName(logBackProperties.getFileDataSource());
                Object instance = clazz.getDeclaredConstructor().newInstance();
                ((LogBackDataSource) instance).save(logBean);
            } else {
                LogFileDataSource fileDefaultDataSource = new LogFileDataSource();
                fileDefaultDataSource.save(logBean);
            }

        } catch (Exception e) {
            log.error("日志AOP封装log对象异常: {}", ExceptionUtil.stacktraceToString(e));
        }

    }


}
