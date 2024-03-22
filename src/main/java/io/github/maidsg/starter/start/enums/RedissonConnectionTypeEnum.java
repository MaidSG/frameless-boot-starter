package io.github.maidsg.starter.start.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************************
 * <pre></pre>
 * @文件名称： RedissonConnectTypeEnum.java
 * @包 路  径： io.github.maidsg.starter.redisson.enums
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/21 16:45
 * @Modify：
 */

@Getter
@AllArgsConstructor
public enum RedissonConnectionTypeEnum {

    /**
     * 单机部署方式(默认)
     */
    STANDALONE("standalone", "单机"),

    /**
     * 哨兵
     */
    SENTINEL("sentinel", "哨兵模式")


     ;

    /**
     * 编码
     */
    private final String code;
    /**
     * 名称
     */
    private final String name;

}
