package io.github.maidsg.starter.start.annotation.desensitization;

import java.lang.annotation.*;

/*******************************************************************
 * <pre></pre>
 * @文件名称： DecodeData.java
 * @包 路  径： io.github.maidsg.starter.start.annotation
 * @Copyright：wy (C) 2024 *
 * @Description: 解密方法返回对象中的敏感字段
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/4/2 18:22
 * @Modify：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DecodeData {

    /**
     * 处理实体类class
     * @return 处理实体类class
     */
    Class entity() default Object.class;

}
