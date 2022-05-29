package com.cnpmHDT.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateOrdersForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;

    @NotNull(message = "ordersAddress cannot be null")
    @ApiModelProperty(required = true)
    private String ordersAddress;

    @NotNull(message = "ordersReceiverName cannot be null")
    @ApiModelProperty(required = true)
    private String ordersReceiverName;

    @NotNull(message = "ordersReceiverPhone cannot be null")
    @ApiModelProperty(required = true)
    private String ordersReceiverPhone;
}
