package io.github.maidsg.framelessbootstarter.annotation;

import io.github.maidsg.framelessbootstarter.dao.LogFileDataSource;
import io.github.maidsg.framelessbootstarter.enums.OperationTypeEnum;
import io.github.maidsg.framelessbootstarter.enums.PlatformTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*******************************************************************
 * <pre></pre>
 * @文件名称： Traceable.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.annotation
 * @Copyright：wy (C) 2024 *
 * @Description: 用于追踪和记录系统中各个接口请求的调用链路和相关日志信息
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 17:40
 * @Modify：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Traceable {

    String desc() default "链路记录";

    Class[] logExpandHandles() default {LogFileDataSource.class};

    PlatformTypeEnum platformType() default PlatformTypeEnum.SYSTEM;

    OperationTypeEnum operationType() default OperationTypeEnum.RETRIEVE;

    /**
     * 记录接口返回结果
     * @return
     */
    boolean apiResult() default false;


}
