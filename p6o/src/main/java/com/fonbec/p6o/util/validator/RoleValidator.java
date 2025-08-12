package com.fonbec.p6o.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fonbec.p6o.security.enums.RoleEnum;

public class RoleValidator implements ConstraintValidator<ValidRoles, List<String>> {

    private static final Set<String> VALID_ROLES = Arrays.stream(RoleEnum.values())
            .map(RoleEnum::name)
            .collect(Collectors.toSet());
            
    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        for (String role : roles) {
            if (!VALID_ROLES.contains(role)) {
                return false;
            }
        }
        return true;
    }
}
