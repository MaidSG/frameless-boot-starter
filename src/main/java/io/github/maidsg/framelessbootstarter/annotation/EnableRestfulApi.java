package io.github.maidsg.framelessbootstarter.annotation;

import io.github.maidsg.framelessbootstarter.config.BeanAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*******************************************************************
 * <pre></pre>
 * @文件名称： EnableRestfulApi.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.annotation
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/2/29 18:12
 * @Modify：
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({BeanAutoConfiguration.class})
public @interface EnableRestfulApi {
}
