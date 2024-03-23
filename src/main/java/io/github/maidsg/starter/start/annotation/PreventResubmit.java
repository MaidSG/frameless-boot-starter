package io.github.maidsg.starter.start.annotation;

import java.lang.annotation.*;

/*******************************************************************
 * <pre></pre>
 * @文件名称： PreventResubmit.java
 * @包 路  径： io.github.maidsg.starter.start.annotation
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 11:13
 * @Modify：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PreventResubmit {

    /**
     * 超时时间 单位秒
     *
     * @return 超时时间 单位秒
     */
    int lockTime();

    /**
     * redis 锁key的
     *
     * @return redis 锁key
     */
    String lockKey() default "resubmit";




}