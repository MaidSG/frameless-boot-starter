package io.github.maidsg.starter.start.strategy.impl;

import io.github.maidsg.starter.start.constant.RedissonConstant;
import io.github.maidsg.starter.start.model.codc.FastJsonCodec;
import io.github.maidsg.starter.start.strategy.RedissonConfigStrategy;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = RedissonConstant.REDIS_CONNECTION_PREFIX + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            config.setCodec(new FastJsonCodec());
            log.info("初始化Redisson单机配置,连接地址:" + address);
        } catch (Exception e) {
            log.error("单机Redisson初始化错误", e);
            e.printStackTrace();
        }
        return config;
    }
}
