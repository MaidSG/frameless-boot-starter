package io.github.maidsg.framelessbootstarter.model.settings;

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
@ConfigurationProperties(prefix = "frameless")
public class BootStarterProperties {

    private boolean enabled = Boolean.FALSE;


    private String success = "success";

    private String code = "code";

    private String codeSuccessValue = "OK";

    private String msg = "msg";

    private String timestamp = "timestamp";

    private String data = "data";


}
