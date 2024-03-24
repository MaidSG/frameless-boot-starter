package io.github.maidsg.starter.start.component;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedissonLockAgent.java
 * @包 路  径： io.github.maidsg.starter.start.component
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 11:45
 * @Modify：
 */
@Slf4j
@Component
public class RedissonLockAgent {

    @Autowired
    private RedissonClient redissonClient;



    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取锁
     * @param key 锁的key
     * @return 锁是否存在
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 解锁
     * @param lockName 锁的名称
     */
    public void unlock(String lockName) {
        if (lockName == null) {
            return;
        }

        // 如果不存在key，就直接返回
        if (!existKey(lockName)) {
            return;
        }

        try {
            redissonClient.getLock(lockName).unlock();
        } catch (Exception e) {
            log.error("解锁异常，lockName=" + lockName, e);
        }
    }

    /**
     * 加锁操作 公平锁
     * 公平锁：先来先得
     * 公平锁的实现是通过Redis的list数据结构实现的，
     * 每个客户端在获取锁的时候都会把自己的标识放到list中，
     * 然后通过lpop命令获取到第一个标识的客户端获取锁。
     * @param lockKey 锁的key
     * @param unit 时间单位
     * @param leaseTime 锁的租约时间
     * @return 是否获取到锁
     */
    public boolean fairLock(String lockKey, TimeUnit unit, int leaseTime) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        try {
            boolean existKey = existKey(lockKey);
            // 已经存在了，就直接返回
            if (existKey) {
                return false;
            }
            return fairLock.tryLock(3, leaseTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
