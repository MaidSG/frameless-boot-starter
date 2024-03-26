package io.github.maidsg.starter.start.component;

import io.github.maidsg.starter.start.constant.RedisConstant;
import io.github.maidsg.starter.start.interfaces.RedisMessageListener;
import io.github.maidsg.starter.start.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedisMessageClient.java
 * @包 路  径： io.github.maidsg.starter.start.component
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/3/26 18:28
 * @Modify：
 */
@Slf4j
@Component
public class RedisMessageClient {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送消息
     *
     * @param handlerName 消息处理器名称
     * @param params     参数
     */
    public void sendMessage(String handlerName, HashMap<String, Object> params) {
        params.put(RedisConstant.REDIS_HANDLER_NAME, handlerName);
        redisTemplate.convertAndSend(RedisConstant.REDIS_TOPIC_NAME, params);
    }

    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(HashMap<String, Object> params) {
        Object handlerName = params.get(RedisConstant.REDIS_HANDLER_NAME);
        RedisMessageListener messageListener = SpringContextHolder.getHandler(handlerName.toString(), RedisMessageListener.class);
        if (!ObjectUtils.isEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
