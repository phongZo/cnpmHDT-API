package com.cnpmHDT.api.validation.impl;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.validation.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidation  implements ConstraintValidator<Status, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Status constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }
    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        if(status == null && allowNull){
            return true;
        }
        if(!Objects.equals(status, cnpmHDTConstant.STATUS_ACTIVE)
                && !Objects.equals(status, cnpmHDTConstant.STATUS_LOCK)
                && !Objects.equals(status, cnpmHDTConstant.STATUS_DELETE)
                && !Objects.equals(status, cnpmHDTConstant.STATUS_PENDING)){
            return false;
        }
        return true;
    }
}
