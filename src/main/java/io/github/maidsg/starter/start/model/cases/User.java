package io.github.maidsg.starter.start.model.cases;

import io.github.maidsg.starter.start.annotation.SpELAssert;
import lombok.Builder;
import lombok.Data;

/*******************************************************************
 * <pre></pre>
 * @文件名称： User.java
 * @包 路  径： io.github.maidsg.starter.start.model.cases
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 10:11
 * @Modify：
 */
@Data
@Builder
public class User {


    @SpELAssert(value = "email.contains('@')", message = "Email should contain @ symbol")
    private String email;

    // ...

}
