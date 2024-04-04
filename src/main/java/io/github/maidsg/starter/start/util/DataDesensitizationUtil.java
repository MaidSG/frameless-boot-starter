package io.github.maidsg.starter.start.util;

import io.github.maidsg.starter.start.annotation.desensitization.ProtectedData;
import io.github.maidsg.starter.start.enums.ProtectedDataTypeEnum;
import io.github.maidsg.starter.start.util.encryption.AesEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/*******************************************************************
 * <pre></pre>
 * @文件名称： DataDesensitizationUtil.java
 * @包 路  径： io.github.maidsg.starter.start.util
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date： 2024/4/4 17:25
 * @Modify：
 */
@Slf4j
public class DataDesensitizationUtil {

    public static void handleNestedObject(Object obj, Class entity, boolean isEncode) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!field.getType().isPrimitive()) {
                field.setAccessible(true);
                if (field.getType().equals(entity)) {
                    handlerObject(field.get(obj), isEncode);
                    break;
                } else if (field.getGenericType() instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    if (pt.getRawType().equals(List.class) && pt.getActualTypeArguments()[0].equals(entity)) {
                        handleList(field.get(obj), entity, isEncode);
                        break;
                    }
                }
            }
        }
    }

    public static void handlerObject(Object obj, boolean isEncode) throws IllegalAccessException {
        if (ObjectUtils.isEmpty(obj)) {
            return;
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtectedData.class) && field.getType().isAssignableFrom(String.class)) {
                field.setAccessible(true);
                String realValue = (String) field.get(obj);
                if (realValue != null && !realValue.isEmpty()) {
                    ProtectedData sf = field.getAnnotation(ProtectedData.class);
                    String value = isEncode ? DataDesensitizationUtil.getEncodeData(realValue, sf.type()) :
                            sf.type().equals(ProtectedDataTypeEnum.ENCODE) ? DataDesensitizationUtil.getDecodeData(realValue) : realValue;
                    field.set(obj, value);
                }
            }
        }
    }

    public static void handleList(Object obj, Class entity, boolean isEncode) {
        List list = (List) obj;
        if (!list.isEmpty() && list.get(0).getClass().equals(entity)) {
            for (Object temp : list) {
                try {
                    handlerObject(temp, isEncode);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理数据 获取解密后的数据
     * @param data
     * @return
     */
    public static String getDecodeData(String data){
        String result = null;
        try {
            result = AesEncryptUtil.desEncrypt(data);
        } catch (Exception exception) {
            log.debug("数据解密错误，原数据:"+data);
        }
        //解决debug模式下，加解密失效导致中文被解密变成空的问题
        if(ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(data)){
            result = data;
        }
        return result;
    }

    /**
     * 处理数据 获取加密后的数据 或是格式化后的数据
     * @param data 字符串
     * @param sensitiveEnum 类型
     * @return 处理后的字符串
     */
    public static String getEncodeData(String data, ProtectedDataTypeEnum sensitiveEnum){
        String result;
        switch (sensitiveEnum){
            case ENCODE:
                try {
                    result = AesEncryptUtil.encrypt(data);
                } catch (Exception exception) {
                    log.error("数据加密错误", exception.getMessage());
                    result = data;
                }
                break;
//            case CHINESE_NAME:
//                result = chineseName(data);
//                break;
//            case ID_CARD:
//                result = idCardNum(data);
//                break;
//            case FIXED_PHONE:
//                result = fixedPhone(data);
//                break;
//            case MOBILE_PHONE:
//                result = mobilePhone(data);
//                break;
//            case ADDRESS:
//                result = address(data, 3);
//                break;
//            case EMAIL:
//                result = email(data);
//                break;
//            case BANK_CARD:
//                result = bankCard(data);
//                break;
//            case CNAPS_CODE:
//                result = cnapsCode(data);
//                break;
            default:
                result = data;
        }
        return result;
    }


}
