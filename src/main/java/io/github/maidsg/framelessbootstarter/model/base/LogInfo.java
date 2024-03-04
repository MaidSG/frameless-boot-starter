package io.github.maidsg.framelessbootstarter.model.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*******************************************************************
 * <pre></pre>
 * @文件名称： LogInfo.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.model.base
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 17:29
 * @Modify：
 */
@Data
@Builder
public class LogInfo {

    /**
     * 日志类型 INFO ERROR
     */
    private String logType;

    /**
     * 请求耗时
     */
    private Long requestTime = 0L;

    /**
     * 请求唯一id
     */
    private String requestId;

    /**
     * 接口返回数据
     */
    private Object result;

    /**
     * 异常详细
     */
    private String exceptionDetail;

    /**
     *  日志切面构造方法
     * @param logType
     * @param time
     */
    public LogInfo(String logType, Long time) {
        this.logType = logType;
        this.requestTime = time;
    }

}
