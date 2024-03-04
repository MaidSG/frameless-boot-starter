package io.github.maidsg.framelessbootstarter.exception;

import io.github.maidsg.framelessbootstarter.enums.ResponseEnum;
import io.github.maidsg.framelessbootstarter.interfaces.BaseCode;
import lombok.Data;

/*******************************************************************
 * <pre></pre>
 * @文件名称： BusinessException.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.exception
 * @Copyright：wy (C) 2024 *
 * @Description: 通用业务异常类
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 11:26
 * @Modify：
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private String code = ResponseEnum.BUSINESS_EXCEPTION.getCode();

    /**
     * 错误提示
     */
    private String message;


    public BusinessException(String message) {
        this.message = message;

    }

    public BusinessException(String message, String code) {
        this.message = message;
        this.code = code;

    }

    public BusinessException(BaseCode resultCode) {
        this.message = resultCode.getMessage();
        this.code = resultCode.getCode();

    }

}
