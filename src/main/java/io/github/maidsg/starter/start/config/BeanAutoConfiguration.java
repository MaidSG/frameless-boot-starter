package io.github.maidsg.starter.start.config;

import io.github.maidsg.starter.start.aop.TraceLogAspect;
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

    @Bean
    public BootStarterProperties bootStarterProperties(){
        return new BootStarterProperties();
    }

    @Bean
    public TraceLogAspect traceLogAspect(){return new TraceLogAspect();}

    @Bean
    public BootStarterProperties.LogBackProperties logBackProperties(BootStarterProperties bootStarterProperties){
        return bootStarterProperties.new LogBackProperties();
    }






}
