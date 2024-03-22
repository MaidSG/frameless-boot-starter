package io.github.maidsg.starter.start.controlleradvice;

import io.github.maidsg.starter.start.annotation.DisableRestfulApi;
import io.github.maidsg.starter.start.model.base.Res;
import io.github.maidsg.starter.start.model.settings.BootStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/*******************************************************************
 * <pre></pre>
 * @文件名称： ResultAdvice.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.controlleradvice
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/1 14:47
 * @Modify：
 */
@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResultAdvice  implements ResponseBodyAdvice<Object> {

    @Resource
    private BootStarterProperties apiProperties;

    /**
     * supports方法: 判断是否要执行beforeBodyWrite方法,
     * true为执行,false不执行.
     * 通过该方法可以选择哪些类或那些方法的response要进行处理, 其他的不进行处理.
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return this.userDefineWrapResult(returnType);
    }


    /**
     * 实际返回结果
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Res) {
            return this.userDefinedResultKey((Res) body);
        } else if (body == null) {
            return this.userDefinedResultKey(Res.ok());
        } else if (body instanceof String) {
            return body;
        } else {
            return this.userDefinedResultKey(Res.successWith(body));
        }
    }

    /**
     * supports方法 判断条件
     *
     * @param returnType
     * @return
     */
    private Boolean userDefineWrapResult(MethodParameter returnType) {

        /**
         * 注解优先级别最高
         */
        Boolean flag = !returnType.hasMethodAnnotation(DisableRestfulApi.class);
        if (!flag) return flag;

       return Boolean.TRUE;
    }


    /**
     *
     * @return
     */
    private Object userDefinedResultKey(Res res){
        Map resultMap = new LinkedHashMap();
        if (null != apiProperties && null != res && apiProperties.isEnabled()){
            String keyCode = apiProperties.getCode();
            String keyMsg = apiProperties.getMsg();
            String keySuccess = apiProperties.getSuccess();
            String keyData = apiProperties.getData();
            String codeSuccessValue = apiProperties.getCodeSuccessValue();

            if (StrUtil.isNotEmpty(keySuccess)) {
                resultMap.put(keySuccess, res.getSuccess());
            }
            if (StrUtil.isNotEmpty(keyCode)) {
                if (StrUtil.isNotEmpty(codeSuccessValue) && res.getCode().equals("OK")) {
                    resultMap.put(keyCode, apiProperties.getCodeSuccessValue());
                } else {
                    resultMap.put(keyCode, res.getCode());
                }
            }
            if (StrUtil.isNotEmpty(keyMsg)) {
                resultMap.put(keyMsg, res.getMsg());
            }
            resultMap.put("requestId", res.getRequestId());
            resultMap.put("timestamp", res.getTimestamp());
            if (StrUtil.isNotEmpty(keyData)) {
                resultMap.put(keyData, res.getData());
            }

            return resultMap;

        }

        return res;

    }




}
