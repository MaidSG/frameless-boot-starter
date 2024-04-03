package io.github.maidsg.starter.start.annotation.desensitization;

import java.lang.annotation.*;

/*******************************************************************
 * <pre></pre>
 * @文件名称： ProtectedData.java
 * @包 路  径： io.github.maidsg.starter.start.annotation
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/4/2 18:13
 * @Modify：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ProtectedData {
}
