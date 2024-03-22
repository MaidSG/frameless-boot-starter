package io.github.maidsg.starter.start.enums;

import io.github.maidsg.starter.start.interfaces.BaseCode;

/*******************************************************************
 * <pre></pre>
 * @文件名称： ResponseEnum.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.enums
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/2/29 19:00
 * @Modify：
 */
public enum ResponseEnum implements BaseCode {

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR("500", "internal_server_error"),
    BAD_GATEWAY("502", "bad_gateway"),
    NOT_FOUND("404", "not_found"),
    UNAUTHORIZED("401", "unauthorized"),
    UNAUTHORIZED_EXPIRED("402","unauthorized_expired"),
    FORBIDDEN("403", "forbidden"),
    METHOD_NOT_ALLOWED("405", "method_not_allowed"),
    REQUEST_TIMEOUT("408", "request_timeout"),

    INVALID_ARGUMENT("A0000", "invalid_argument : {}"),
    ARGUMENT_ANALYZE("A0001", "argument_analyze"),

    BUSINESS_EXCEPTION("B0000", "business_exception"),


    ;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
