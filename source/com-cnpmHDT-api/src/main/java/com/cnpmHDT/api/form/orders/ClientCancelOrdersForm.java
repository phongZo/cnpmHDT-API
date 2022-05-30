package com.cnpmHDT.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClientCancelOrdersForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;
}
