package io.github.maidsg.starter.start.config;

import com.alibaba.fastjson2.JSON;
import io.github.maidsg.starter.start.component.RedisLockCacheWriter;
import io.github.maidsg.starter.start.component.serializers.RedisFastJson2Serializer;
import io.github.maidsg.starter.start.constant.RedissonConstant;
import io.github.maidsg.starter.start.manager.RedissonManager;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

import static java.util.Collections.singletonMap;

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
@EnableConfigurationProperties({BootStarterProperties.RedissonProperties.class, RedisProperties.class})
public class RedissonConfiguration {


    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "frameless.redisson", value = "enabled", havingValue = "true")
    public RedissonClient redissonClient(BootStarterProperties.RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        log.info("RedissonManager初始化完成,当前连接方式:" + redissonProperties.getType() + ",连接地址:" + redissonProperties.getAddress());
        return redissonManager.getRedisson();
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("====初始化RedisTemplate=====");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置序列化
        RedisFastJson2Serializer<Object> fastJson2RedisSerializer = new RedisFastJson2Serializer<>(Object.class);
        StringRedisSerializer serializer = new StringRedisSerializer();
        // 采用stringRedisSerializer序列化key，这样key不会带有引号
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setValueSerializer(fastJson2RedisSerializer);
        redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);
        // bean的生命周期，属性填充完毕后调用
        redisTemplate.afterPropertiesSet();
        log.info("====初始化RedisTemplate完成=====");
        return redisTemplate;
    }

    /**
     * 自定义缓存key生成策略
     * 使用方法名+参数作为key，不会出现empty key
     */
    @Component("defaultKeyGenerate")
    public static class SelfKeyGenerate implements KeyGenerator {
        @Override
        public Object generate(Object target, Method method, Object... params) {
            return target.getClass().getSimpleName() + "#" + method.getName() + "(" + JSON.toJSONString(params) + ")";
        }
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisFastJson2Serializer<Object> fastJson2RedisSerializer = new RedisFastJson2Serializer<>(Object.class);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(3));

        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJson2RedisSerializer));

        RedisCacheWriter writer = new RedisLockCacheWriter(redisConnectionFactory, Duration.ofMillis(50L));

        log.info("====初始化CacheManager完成=====");

        return RedisCacheManager.builder(writer)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(singletonMap(RedissonConstant.TEST_DEMO_CACHE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)).disableCachingNullValues()))
                .transactionAware()
                .build();
    }


}
