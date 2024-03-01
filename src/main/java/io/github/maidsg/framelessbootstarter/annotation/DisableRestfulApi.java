package io.github.maidsg.framelessbootstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*******************************************************************
 * <pre></pre>
 * @文件名称： DisableRestfulApi.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.annotation
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/1 14:51
 * @Modify：
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableRestfulApi {
}
