package com.cnpmHDT.api.validation.impl;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.validation.OrdersState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrdersStateValidation implements ConstraintValidator<OrdersState, Integer> {
    private boolean allowNull;
    @Override
    public void initialize(OrdersState constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer ordersState, ConstraintValidatorContext constraintValidatorContext) {
        if(ordersState == null && allowNull){
            return true;
        }
        if(!ordersState.equals(cnpmHDTConstant.ORDERS_STATE_CREATED)
            &&!ordersState.equals(cnpmHDTConstant.ORDERS_STATE_DONE)
            &&!ordersState.equals(cnpmHDTConstant.ORDERS_STATE_CANCELED)){
                return false;
        }
        return true;
    }
}
