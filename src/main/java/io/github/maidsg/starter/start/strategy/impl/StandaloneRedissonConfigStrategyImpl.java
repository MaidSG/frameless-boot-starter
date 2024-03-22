package io.github.maidsg.starter.start.strategy.impl;

import io.github.maidsg.starter.start.strategy.RedissonConfigStrategy;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;

/*******************************************************************
 * <pre></pre>
 * @文件名称： StandaloneRedissonConfigStrategyImpl.java
 * @包 路  径： io.github.maidsg.starter.redisson.strategy.impl
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 17:03
 * @Modify：
 */
@Slf4j
public class StandaloneRedissonConfigStrategyImpl implements RedissonConfigStrategy {
    @Override
    public Config createRedissonConfig(BootStarterProperties.RedissonProperties redissonProperties) {
        return null;
    }
}
