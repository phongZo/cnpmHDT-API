package com.cnpmHDT.api.form.ordersdetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateOrdersDetailForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersDetailId;

    @NotNull(message = "ordersDetailPrice cannot be null")
    @ApiModelProperty(required = true)
    private Double ordersDetailPrice;

    @NotNull(message = "ordersDetailAmount cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersDetailAmount;
}
