package org.example.blog.anno;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;
import org.example.blog.validation.StateValidation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented // 元注解
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {StateValidation.class}) // 指定校验逻辑所在的类
public @interface State {
    String message() default "state参数的值只能是已发布或草稿";

    // 分组
    Class<?>[] groups() default {};

    // 负载 获取State注解的有效负载
    Class<? extends Payload>[] payload() default {};
}
