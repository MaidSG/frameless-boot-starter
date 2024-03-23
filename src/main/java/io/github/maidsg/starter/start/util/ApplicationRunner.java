package io.github.maidsg.starter.start.util;

import io.github.maidsg.starter.start.model.cases.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*******************************************************************
 * <pre></pre>
 * @文件名称： AppicationRunner.java
 * @包 路  径： io.github.maidsg.starter.start.util
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 10:07
 * @Modify：
 */
@Component
public class ApplicationRunner implements CommandLineRunner {



    @Override
    public void run(String... args) throws Exception {

        User build = User.builder().email("test.com").build();


    }
}
