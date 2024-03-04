package io.github.maidsg.framelessbootstarter.enums;

import lombok.Getter;

/*******************************************************************
 * <pre></pre>
 * @文件名称： PlatformTypeEnum.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.enums
 * @Copyright：wy (C) 2024 *
 * @Description: 日志记录类型
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 17:46
 * @Modify：
 */
@Getter
public enum PlatformTypeEnum {

    SYSTEM("系统"),
    THIRD_PARTY("第三方");

    private final String desc;

    PlatformTypeEnum(String desc) {
        this.desc = desc;
    }


}
