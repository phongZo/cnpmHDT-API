package com.cnpmHDT.api.validation.impl;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.validation.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GenderValidation implements ConstraintValidator<Gender, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Gender constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer gender, ConstraintValidatorContext constraintValidatorContext) {
        if(gender == null && allowNull){
            return true;
        }
        if(!Objects.equals(gender, cnpmHDTConstant.GENDER_FEMALE)
                && !Objects.equals(gender, cnpmHDTConstant.GENDER_MALE)
                && !Objects.equals(gender, cnpmHDTConstant.GENDER_OTHER)){
            return false;
        }
        return true;
    }
}
