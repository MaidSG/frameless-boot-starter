package io.github.maidsg.starter.start.controlleradvice;

import io.github.maidsg.starter.start.enums.ResponseEnum;
import io.github.maidsg.starter.start.exception.BusinessException;
import io.github.maidsg.starter.start.model.base.Res;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*******************************************************************
 * <pre></pre>
 * @文件名称： GlobalExceptionAdvice.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.controlleradvice
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 11:28
 * @Modify：
 */
@Slf4j
@RestControllerAdvice
//  条件注解， 在基于 Servlet 的 Web 应用条件下才生效
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionAdvice {


    /**
     * 系统异常全局捕获
     *
     * @param e Exception
     * @return Res
     */
    @ExceptionHandler(Exception.class)
    public Res error(Exception e) {
        log.error("系统异常: {}", ExceptionUtil.stacktraceToString(e));
        return Res.failWith(ResponseEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 统一业务异常处理
     *
     * @param e BusinessException
     * @return Res
     */
    @ExceptionHandler(BusinessException.class)
    public Res<Object> error(BusinessException e) {
        return Res.failWith(e.getCode(), e.getMessage());
    }

    /**
     * 接口不存在
     * @param e NoHandlerFoundException
     * @return Res
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Res<Object> error(NoHandlerFoundException e){
        return Res.failWith(ResponseEnum.NOT_FOUND,e.getRequestURL());
    }

    /**
     * 请求方法不被允许
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Res<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Res.failWith(ResponseEnum.METHOD_NOT_ALLOWED, ExceptionUtil.stacktraceToString(e));
    }

    /**
     * 请求与响应媒体类型不一致 异常
     *
     * @param e HttpMediaTypeNotSupportedException
     * @return Res
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Res<Object> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return Res.failWith(ResponseEnum.BAD_GATEWAY, ExceptionUtil.stacktraceToString(e));
    }

    /**
     * body json参数解析异常
     *
     * @param e HttpMessageNotReadableException
     * @return Res
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Res<Object> HttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        request.setAttribute(ResponseEnum.INVALID_ARGUMENT.getMessage(), e.getMessage());
        return Res.failWith(ResponseEnum.INVALID_ARGUMENT.getCode(),
                StrUtil.format(ResponseEnum.INVALID_ARGUMENT.getMessage(),e.getMessage()), ExceptionUtil.stacktraceToString(e));
    }

    /**
     * 验证  对象类型参数 JSON body 参数
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<Object> jsonParamsException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("#参数异常# {}", ExceptionUtil.stacktraceToString(e));
        BindingResult bindingResult = e.getBindingResult();
        List errorList = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s%s；", fieldError.getField(), fieldError.getDefaultMessage());
            errorList.add(msg);
        }
        request.setAttribute(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(errorList, ";"));
        return Res.failWith(ResponseEnum.INVALID_ARGUMENT.getCode(),
                StrUtil.format(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(errorList, ";")));
    }

    /**
     * 验证 单个参数类型
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Res<Object> ConstraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {
        List errorList = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            StringBuilder message = new StringBuilder();
            Path path = violation.getPropertyPath();
            String msg = message.append(((PathImpl) path).getLeafNode()).append(violation.getMessage()).toString();
            errorList.add(msg);
        }
        request.setAttribute(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(errorList, ";"));
        return Res.failWith(ResponseEnum.INVALID_ARGUMENT.getCode(),
                StrUtil.format(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(errorList, ";")));
    }

    @ExceptionHandler(BindException.class)
    public Res<Object> BindExceptionHandler(BindException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + o.getDefaultMessage())
                .collect(Collectors.toList());
        request.setAttribute(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(collect, ";"));
        return Res.failWith(ResponseEnum.INVALID_ARGUMENT.getCode(),
                StrUtil.format(ResponseEnum.INVALID_ARGUMENT.getMessage(), CollUtil.join(collect, ";")));
    }


}
