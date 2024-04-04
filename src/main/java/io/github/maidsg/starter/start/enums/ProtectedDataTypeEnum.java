package io.github.maidsg.starter.start.enums;

import lombok.Getter;

/*******************************************************************
 * <pre></pre>
 * @文件名称： ProtectedDataTypeEnum.java
 * @包 路  径： io.github.maidsg.starter.start.enums
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/4/2 18:30
 * @Modify：
 */
@Getter
public enum ProtectedDataTypeEnum {

    /**
     * 加密 通用AES加密
     */
    ENCODE,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 手机号
     */
    MOBILE_PHONE,

    /**
     * 电子邮件
     */
    EMAIL,

    /**
     * 银行卡
     */
    BANK_CARD,

    ;


}
