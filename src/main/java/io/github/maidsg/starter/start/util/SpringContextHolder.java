package io.github.maidsg.starter.start.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;

/*******************************************************************
 * <pre></pre>
 * @文件名称： SpringContextHolder.java
 * @包 路  径： io.github.maidsg.starter.start.util
 * @Copyright：wy (C) 2024 *
 * @Description: 静态变量保存Spring ApplicationContext用于操作
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/3/26 18:55
 * @Modify：
 */
@Slf4j
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getHandler(String name, Class<T> cls) {
        T t = null;
        if (!ObjectUtils.isEmpty(name)) {
            checkApplicationContext();
            try {
                t = applicationContext.getBean(name, cls);
            } catch (Exception e) {
                log.warn("Customize redis listener handle [ " + name + " ], does not exist！");
            }
        }
        return t;
    }


    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

}
