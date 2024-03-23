package io.github.maidsg.starter.start.config;

import io.github.maidsg.starter.start.manager.RedissonManager;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedissonConfiguration.java
 * @包 路  径： io.github.maidsg.starter.redisson.config
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 16:36
 * @Modify：
 */

@Slf4j
@Configuration
@ConditionalOnClass(BootStarterProperties.RedissonProperties.class)
@EnableConfigurationProperties(BootStarterProperties.RedissonProperties.class)
public class RedissonConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "frameless.redisson", value = "enabled", havingValue = "true")
    public RedissonClient redissonClient(BootStarterProperties.RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        log.info("RedissonManager初始化完成,当前连接方式:" + redissonProperties.getType() + ",连接地址:" + redissonProperties.getAddress());
        return redissonManager.getRedisson();
    }

}
