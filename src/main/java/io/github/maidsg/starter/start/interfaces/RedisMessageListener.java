package io.github.maidsg.starter.start.interfaces;

import java.util.HashMap;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedisMessageListener.java
 * @包 路  径： io.github.maidsg.starter.start.interfaces
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/3/26 18:48
 * @Modify：
 */
public interface RedisMessageListener {
    /**
     * 接受消息
     *
     * @param message  消息
     */
    void onMessage(HashMap<String, Object>  message);
}
