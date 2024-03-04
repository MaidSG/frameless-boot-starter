package io.github.maidsg.framelessbootstarter.model.base;

import com.alibaba.fastjson2.annotation.JSONType;
import io.github.maidsg.framelessbootstarter.interfaces.BaseCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*******************************************************************
 * <pre></pre>
 * @文件名称： Res.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.model.base
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/2/29 18:51
 * @Modify：
 */
@Data
@Slf4j
@NoArgsConstructor
@JSONType(orders = {"success", "code", "msg", "requestId", "timestamp", "data"})
public class Res<T> extends AbstractSerialObject {

    public static final String SUCCESS_CODE = "OK";
    public static final String ERROR_CODE = "FAIL";
    public static final String MSG = "操作成功";
    public static final String ERROR_MSG = "操作失败";

    private Boolean success;

    private String code;

    private String msg;

    private String requestId = IdUtil.nanoId(20);

    private String timestamp = DateUtil.formatNow();

    private T data;

    public Res(Boolean success, String code, String msg, T data) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构建返回结果
     *
     * @param success
     * @param resultCode
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Res<T> build(Boolean success, BaseCode resultCode, T result) {
        return new Res(success, resultCode.getCode(), resultCode.getMessage(), result);
    }

    /**
     * 构建返回结果 自定义响应码信息
     *
     * @param success
     * @param msg
     * @param code
     * @param result
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Res<T> build(Boolean success, String code, String msg, T result) {
        return new Res(success, code, msg, result);
    }

    /**
     * 构建成功结果
     *
     * @return
     */
    public static <T> Res<T> ok() {
        return build(Boolean.TRUE, SUCCESS_CODE, MSG, null);
    }



    /**
     * 构建成功结果
     *
     * @param result
     * @return
     */
    public static <T> Res<T> successWith(T result) {
        return build(Boolean.TRUE, SUCCESS_CODE, MSG, result);
    }


    /**
     * 构建成功结果
     *
     * @param msg
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Res<T> successWith(String msg, T result) {
        return build(Boolean.TRUE, SUCCESS_CODE, msg, result);
    }

    /**
     * 构建失败结果
     *
     * @param msg
     * @param code
     * @param result
     * @return
     */
    public static <T> Res<T> failWith(String code, String msg, T result) {
        return build(Boolean.FALSE, code, msg, result);
    }

    /**
     * 构建失败结果
     *
     * @param msg
     * @param code
     * @return
     */
    public static <T> Res<T> failWith(String code, String msg) {
        return build(Boolean.FALSE, code, msg, null);
    }

    /**
     * 构建失败结果
     *
     * @param resultCode
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Res<T> failWith(BaseCode resultCode, T result) {
        return build(Boolean.FALSE, resultCode, result);
    }

    /**
     * 构建失败结果
     *
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Res<T> failWith(BaseCode resultCode) {
        return build(Boolean.FALSE, resultCode, null);
    }


}