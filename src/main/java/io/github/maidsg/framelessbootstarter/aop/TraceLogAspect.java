package io.github.maidsg.framelessbootstarter.aop;

import io.github.maidsg.framelessbootstarter.enums.LogLevelEnum;
import io.github.maidsg.framelessbootstarter.interfaces.LogBackDataSource;
import io.github.maidsg.framelessbootstarter.model.base.LogInfo;
import io.github.maidsg.framelessbootstarter.model.base.Res;
import io.github.maidsg.framelessbootstarter.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*******************************************************************
 * <pre></pre>
 * @文件名称： TraceLogAspect.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.aop
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

    @Resource
    private BootStarterProperties.LogBackProperties logBackProperties;

    private long currentTime = 0L;

    @Pointcut("@annotation(io.github.maidsg.framelessbootstarter.annotation.Traceable)")
    public void logPointcut() {
    }

    private LogInfo getResponseLogInfo(Object result) {
        //判断返回结果
        LogInfo logBean = new LogInfo(LogLevelEnum.INFO.name(), System.currentTimeMillis() - currentTime);

        if (result instanceof Res) {
            Res res = (Res) result;
            if (!res.getSuccess() || res.getCode() != Res.SUCCESS_CODE) {
                logBean.setLogType(LogLevelEnum.ERROR.name());
                logBean.setExceptionDetail(res.getMsg());
                logBean.setRequestId(res.getRequestId());
            } else {
                logBean.setResult(res.getData());
            }
        }
        return logBean;
    }


}
