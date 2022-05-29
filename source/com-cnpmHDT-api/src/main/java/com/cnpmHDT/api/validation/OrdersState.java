package com.cnpmHDT.api.validation;


import com.cnpmHDT.api.validation.impl.OrdersStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrdersStateValidation.class)
@Documented
public @interface OrdersState {
    boolean allowNull() default true;
    String message() default "Order state invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
