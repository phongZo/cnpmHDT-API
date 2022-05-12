package com.cnpmHDT.api.form.ordersdetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateOrdersDetailForm {
    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @NotNull(message = "ordersDetailAmount cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersDetailAmount;
}
