package io.github.maidsg.starter.start.component.aspect;

import io.github.maidsg.starter.start.annotation.desensitization.DecodeData;
import io.github.maidsg.starter.start.annotation.desensitization.EncodeData;
import io.github.maidsg.starter.start.util.DataDesensitizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/*******************************************************************
 * <pre></pre>
 * @文件名称： DataDesensitizationAspect.java
 * @包 路  径： io.github.maidsg.starter.start.component.aspect
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/4/4 17:20
 * @Modify：
 */
@Slf4j
@Aspect
@Component
public class DataDesensitizationAspect {

    /**
     * 定义切点Pointcut
     */
    @Pointcut("@annotation(io.github.maidsg.starter.start.annotation.desensitization.EncodeData) || @annotation(io.github.maidsg.starter.start.annotation.desensitization.DecodeData)")
    public void sensitivePointCut() {
    }

    @Around("sensitivePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 处理结果
        Object result = joinPoint.proceed();
        if(result == null){
            return result;
        }
        Class resultClass = result.getClass();
        log.debug(" resultClass  = {}" , resultClass);

        if(resultClass.isPrimitive()){
            //是基本类型 直接返回 不需要处理
            return result;
        }

        // 获取方法注解信息：是哪个实体、是加密还是解密
        boolean isEncode = true;
        Class entity = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        EncodeData encode = method.getAnnotation(EncodeData.class);
        if(encode==null){
            DecodeData decode = method.getAnnotation(DecodeData.class);
            if(decode!=null){
                entity = decode.entity();
                isEncode = false;
            }
        }else{
            entity = encode.entity();
        }

        long startTime=System.currentTimeMillis();
        if(resultClass.equals(entity) || entity.equals(Object.class)){
            // 方法返回实体和注解的entity一样，如果注解没有申明entity属性则认为是(方法返回实体和注解的entity一样)
            DataDesensitizationUtil.handlerObject(result, isEncode);
        } else if(result instanceof List){
            // 方法返回List<实体>
            DataDesensitizationUtil.handleList(result, entity, isEncode);
        }else{
            // 方法返回一个对象
            DataDesensitizationUtil.handleNestedObject(result, entity, isEncode);
        }
        long endTime=System.currentTimeMillis();
        log.info((isEncode ? "加密操作，" : "解密操作，") + "Aspect程序耗时：" + (endTime - startTime) + "ms");

        return result;

    }

}
