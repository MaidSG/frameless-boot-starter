package io.github.maidsg.starter.start;

import io.github.maidsg.starter.start.model.cases.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*******************************************************************
 * <pre></pre>
 * @文件名称： UserTest.java
 * @包 路  径： io.github.maidsg.starter.start
 * @Copyright：wy (C) 2024 *
 * @Description:
 * @Version: V1.0
 * @Author： wy
 * @Date：2024/3/23 10:16
 * @Modify：
 */
@SpringBootTest
public class UserTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

//    @Test
//    public void testEmailValidation() {
//        User user = User.builder().email("test.com").build();
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertTrue(violations.isEmpty(), "Email should be valid");
//
//        user = User.builder().email("test").build();
//        violations = validator.validate(user);
//        assertTrue(!violations.isEmpty(), "Email should be invalid");
//    }

}
