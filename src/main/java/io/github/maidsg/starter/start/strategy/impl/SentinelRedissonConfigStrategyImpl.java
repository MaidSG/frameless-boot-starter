package io.github.maidsg.starter.start.strategy.impl;

import io.github.maidsg.starter.start.constant.RedisConstant;
import io.github.maidsg.starter.start.component.serializers.RedissonFastJsonCodec;
import io.github.maidsg.starter.start.strategy.RedissonConfigStrategy;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/*******************************************************************
 * <pre></pre>
 * @文件名称： SentinelRedissonConfigStrategyImpl.java
 * @包 路  径： io.github.maidsg.starter.redisson.strategy.impl
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 17:05
 * @Modify：
 */
@Slf4j
public class SentinelRedissonConfigStrategyImpl implements RedissonConfigStrategy {
    @Override
    public Config createRedissonConfig(BootStarterProperties.RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            // 设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            // 设置哨兵节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress(RedisConstant.REDIS_CONNECTION_PREFIX + addrTokens[i]);
            }
            config.setCodec(new RedissonFastJsonCodec());
            log.info("初始化哨兵方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("哨兵Redisson初始化错误", e);
            e.printStackTrace();
        }
        return config;
    }
}
