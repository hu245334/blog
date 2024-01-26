package org.example.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.blog.anno.State;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StateValidation implements ConstraintValidator<State, String> {

    private static final Set<String> VALID_STATES = new HashSet<>(Arrays.asList("已发布", "草稿"));

    /***
     * @param value   将来要校验的参数值
     * @param context context in which the constraint is evaluated
     * @return 如果false，说明校验失败，如果true，说明校验成功n
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VALID_STATES.contains(value);
    }
}
