package io.github.maidsg.starter.start.constant;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedissonConstant.java
 * @包 路  径： io.github.maidsg.starter.redisson.constant
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 16:57
 * @Modify：
 */
public class RedisConstant {

    /**
     * Redis地址连接前缀
     */
    public static final String REDIS_CONNECTION_PREFIX = "redis://";

    /**
     * 锁的key前缀
     */
    public static final String LOCK_KEY_PREFIX = "lock:";


    /**
     * 测试缓存key
     */
    public static final String TEST_DEMO_CACHE = "redis_manage:demo";

    /**
     * redis消息名称
     */
    public static final String REDIS_HANDLER_NAME = "redis_handler_name";

    /**
     * redis消息主题名称
     */
    public static final String REDIS_TOPIC_NAME = "redis_message_topic";


}
