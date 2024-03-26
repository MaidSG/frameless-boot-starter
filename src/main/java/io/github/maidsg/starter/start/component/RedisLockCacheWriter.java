package io.github.maidsg.starter.start.component;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedisLockCacheWriter.java
 * @包 路  径： io.github.maidsg.starter.start.component
 * @Copyright：wy (C) 2024 *
 * @Description: reference from DefaultRedisCacheWriter
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/3/26 13:49
 * @Modify：
 */
@Slf4j
public class RedisLockCacheWriter implements RedisCacheWriter {

    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;
    private final CacheStatisticsCollector statistics = CacheStatisticsCollector.create();

    private static final String ALL_KEYS = "*";

    public RedisLockCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO);
    }

    public RedisLockCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {
        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
    }


    /**
     * 添加前缀
     *
     * @param key 键
     * @return 带前缀的键
     */
    private static byte[] createCacheLockKey(String key) {
        return ("spring-cache-lock-" + key).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 根据sleepTime判断是否需要加锁
     *
     * @return 是否加锁
     */
    private boolean isLockingCacheWriter() {
        return !this.sleepTime.isZero() && !this.sleepTime.isNegative();
    }

    /**
     * 判断是否需要过期
     *
     * @param ttl 过期时间
     * @return 是否过期
     */
    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    /**
     * 通过redisConnection判断key是否存在
     *
     * @param key        键
     * @param connection 连接
     * @return 是否存在
     */
    private boolean isKeyLocked(String key, RedisConnection connection) {
        return Boolean.TRUE.equals(connection.exists(createCacheLockKey(key)));
    }

    /**
     * @param name      缓存名称
     * @param connection 连接
     * @return 是否加锁成功
     */
    private boolean doLock(String name, RedisConnection connection) {
        return Boolean.TRUE.equals(connection.setNX(createCacheLockKey(name), new byte[0]));
    }

    /**
     * @param name       缓存名称
     * @param connection 连接
     * @return 是否解锁成功
     */
    private Long doUnlock(String name, RedisConnection connection) {
        return connection.del(new byte[][]{createCacheLockKey(name)});
    }

    /**
     * Execute the given action on a {@link RedisConnection}.
     * 写入前判断是否存在锁
     *
     * @param name       the cache name
     * @param connection the connection provider
     */
    private void checkAndWaitForUnlock(String name, RedisConnection connection) {
        if (this.isLockingCacheWriter()) {
            try {
                while (this.isKeyLocked(name, connection)) {
                    Thread.sleep(this.sleepTime.toMillis());
                }

            } catch (InterruptedException var4) {
                Thread.currentThread().interrupt();
                throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name), var4);
            }
        }
    }

    /**
     * 执行操作
     *
     * @param name    缓存名称
     * @param callback 回调
     * @param <T>    返回类型
     * @return 返回值
     */
    private <T> T execute(String name, Function<RedisConnection, T> callback) {
        RedisConnection connection = this.connectionFactory.getConnection();
        try {
            this.checkAndWaitForUnlock(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }


    @Override
    public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        // 根据ttl判断是否需要设置过期时间
        this.execute(name, (connection) -> {
            connection.set(key, value);
            if (shouldExpireWithin(ttl)) {
                connection.pExpire(key, ttl.toMillis());
                return "success";
            }
            connection.set(key, value);
            return "success";
        });
    }

    @Override
    public byte[] get(@Nullable String name, @Nullable byte[] key) {
        return this.execute(name, (connection) -> {
            assert key != null;
            return connection.get(key);
        });
    }

    @Override
    public byte[] putIfAbsent(@Nullable String name, @Nullable byte[] key, @Nullable byte[] value, @Nullable Duration ttl) {
        return this.execute(name, (connection) -> {
            if (this.isLockingCacheWriter()) {
                this.doLock(name, connection);
            }

            try {
                boolean put;
                if (shouldExpireWithin(ttl)) {
                    assert key != null;
                    assert value != null;
                    put = Boolean.TRUE.equals(connection.set(key, value, Expiration.from(ttl), RedisStringCommands.SetOption.ifAbsent()));
                } else {
                    assert key != null;
                    assert value != null;
                    put = Boolean.TRUE.equals(connection.setNX(key, value));
                }

                if (put) {
                    assert name != null;
                    statistics.incPuts(name);
                    return null;
                }

                return connection.get(key);
            } finally {
                if (this.isLockingCacheWriter()) {
                    this.doUnlock(name, connection);
                }
            }

        });
    }

    @Override
    public void remove(@Nullable String name, @Nullable byte[] key) {
        assert key != null;
        String keyString = new String(key);
        log.info("redis remove key:" + keyString);
        if (StrUtil.isNotBlank(keyString) && keyString.endsWith(ALL_KEYS)) {
            this.execute(name, (connection) -> {
                Set<byte[]> keys = connection.keys(key);
                Long delNum = 0L;
                if (keys != null && !keys.isEmpty()) {
                    delNum = connection.del(keys.toArray(new byte[0][]));
                }
                return delNum;
            });
        } else {
            this.execute(name, (connection) -> connection.del(key));
        }
    }

    @Override
    public void clean(@Nullable String name, @Nullable byte[] pattern) {
        this.execute(name, connection -> {

            boolean wasLocked = false;

            try {

                if (isLockingCacheWriter()) {
                    doLock(name, connection);
                    wasLocked = true;
                }

                // 使用键的策略清除缓存
                long deleteCount = BatchStrategies.keys().cleanCache(connection, Objects.requireNonNull(name), Objects.requireNonNull(pattern));
                while (deleteCount > Integer.MAX_VALUE) {
                    statistics.incDeletesBy(name, Integer.MAX_VALUE);
                    deleteCount -= Integer.MAX_VALUE;
                }
                statistics.incDeletesBy(name, (int) deleteCount);

            } finally {

                if (wasLocked && isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }

            return "OK";
        });
    }

    @Override
    public void clearStatistics(@Nullable String name) {
        statistics.reset(Objects.requireNonNull(name));
    }

    @Override
    @SuppressWarnings(" NullableProblems")
    public RedisCacheWriter withStatisticsCollector(@Nullable CacheStatisticsCollector cacheStatisticsCollector) {
        log.error("RedisLockCacheWriter does not support custom CacheStatisticsCollector");
        return new RedisLockCacheWriter(connectionFactory, sleepTime);
    }

    @Override
    @SuppressWarnings(" NullableProblems")
    public CacheStatistics getCacheStatistics(@Nullable String cacheName) {
        return statistics.getCacheStatistics(Objects.requireNonNull(cacheName));
    }
}
