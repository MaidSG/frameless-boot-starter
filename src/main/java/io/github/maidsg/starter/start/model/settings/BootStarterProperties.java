package io.github.maidsg.starter.start.model.settings;

import io.github.maidsg.starter.start.enums.RedissonConnectionTypeEnum;
import io.github.maidsg.starter.start.constant.StarterConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*******************************************************************
 * <pre></pre>
 * @文件名称： BootStarterProperties.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.model.settings
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/1 14:54
 * @Modify：
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "frameless.api")
public class BootStarterProperties {

    private boolean enabled = Boolean.FALSE;

    private String success = "success";

    private String code = "code";

    private String codeSuccessValue = "OK";

    private String msg = "msg";

    private String timestamp = "timestamp";

    private String data = "data";


    @Configuration
    @ConfigurationProperties(prefix = "frameless.logback")
    @Data
    public class LogBackProperties{
        // 日志路径
        private String logPath ;
        // 日志级别
        private String globalLevel;
        // 日志数据源
        private String fileDataSource = StarterConstant.FILE_SOURCE_PATH;
        // 最大保存天数
        private String maxMaintainDays;
        // 单个文件大小
        private String maxFileSize;

        // 总文件大小超过多少时压缩
        private String totalSizeCap;

    }

    @Data
    @ConfigurationProperties(prefix = "frameless.redisson")
    public class RedissonProperties{

        private String address;
        private String port;
        private RedissonConnectionTypeEnum type = RedissonConnectionTypeEnum.SINGLE;
        private String password;
        private int database;
        private Boolean enabled = false;

    }

    @Data
    @ConfigurationProperties(prefix = "frameless.redis")
    public static class StarterRedisProperties{
        private Boolean enableCache = Boolean.FALSE;
        private Boolean enableMessage = Boolean.FALSE;
        // hours
        private Integer cacheExpireTime = 1;
    }


}
