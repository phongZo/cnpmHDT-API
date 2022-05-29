package com.cnpmHDT.api.form.ordersdetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class DeleteOrdersDetailForm {
    @NotNull(message = "orderDetailId cannot be null")
    @ApiModelProperty(required = true)
    private Long orderDetailId;
}
