package io.github.maidsg.starter.start.strategy;

import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import org.redisson.config.Config;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedissonConfigStrategy.java
 * @包 路  径： io.github.maidsg.starter.redisson.strategy
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 17:03
 * @Modify：
 */
public interface RedissonConfigStrategy {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     *
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(BootStarterProperties.RedissonProperties redissonProperties);

}
