package io.github.maidsg.framelessbootstarter.dao;

import io.github.maidsg.framelessbootstarter.interfaces.LogBackDataSource;
import io.github.maidsg.framelessbootstarter.model.base.LogInfo;
import lombok.extern.slf4j.Slf4j;

/*******************************************************************
 * <pre></pre>
 * @文件名称： LogFileDataSource.java
 * @包 路  径： io.github.maidsg.framelessbootstarter.dao
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/4 17:32
 * @Modify：
 */
@Slf4j
public class LogFileDataSource implements LogBackDataSource {
    @Override
    public void save(LogInfo logInfo) {
        log.info("#save log#: {}", logInfo.toString());
    }
}
