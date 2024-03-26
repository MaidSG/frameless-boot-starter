package io.github.maidsg.starter.start.config;

import io.github.maidsg.starter.start.component.aspect.PreventResubmitAspect;
import io.github.maidsg.starter.start.component.aspect.TraceLogAspect;
import io.github.maidsg.starter.start.component.RedissonLockAgent;
import io.github.maidsg.starter.start.controlleradvice.GlobalExceptionAdvice;
import io.github.maidsg.starter.start.controlleradvice.ResultAdvice;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import org.dromara.hutool.extra.spring.EnableSpringUtil;
import org.springframework.context.annotation.Bean;

/*******************************************************************
 * <pre></pre>
 * @文件名称： BeanAutoConfiguration.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.config
 * @Copyright：wy (C) 2024 *
 * @Description:
 * <p>
 *     作为starter的配置类，必须要有@EnableSpringUtil注解，否则无法注入spring的bean，
 *     `@EnableSpringUtil`注解是为了能够注入spring的bean，
 *     因为starter是一个jar包，不会被spring扫描到,因此只有在配置类中声明的bean才会被spring扫描到
 * </>
 *
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/1 14:43
 * @Modify：
 */
@EnableSpringUtil
public class BeanAutoConfiguration {

    @Bean
    public ResultAdvice resultHandler() {
        return new ResultAdvice();
    }

    @Bean
    public GlobalExceptionAdvice exceptionHandler(){return new GlobalExceptionAdvice();}

    // ==================== settings ====================

    @Bean
    public BootStarterProperties bootStarterProperties(){
        return new BootStarterProperties();
    }

    @Bean
    public BootStarterProperties.LogBackProperties logBackProperties(BootStarterProperties bootStarterProperties){
        return bootStarterProperties.new LogBackProperties();
    }


    // ==================== log ====================

    @Bean
    public TraceLogAspect traceLogAspect(){return new TraceLogAspect();}



    // ==================== redisson ====================

    @Bean
    public RedissonConfiguration.SelfKeyGenerate selfKeyGenerate(){return new RedissonConfiguration.SelfKeyGenerate();}

    @Bean
    public RedissonLockAgent redissonLockAgent(){return new RedissonLockAgent();}

    @Bean
    public PreventResubmitAspect preventResubmitAspect(){return new PreventResubmitAspect();}


}
