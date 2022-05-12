package com.cnpmHDT.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class UpdateOrdersForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;

    @NotEmpty(message = "Customer cannot be null")
    @ApiModelProperty(required = true)
    private String ordersCustomer;

    @ApiModelProperty(name = "ordersSaleOff")
    private Integer ordersSaleOff;

    @NotEmpty(message = "ordersVat cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersVat;

    @NotEmpty(message = "ordersState cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersState;

    @NotEmpty(message = "ordersPrevState cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersPrevState;

    @NotNull(message = "ordersAddress cannot be null")
    @ApiModelProperty(required = true)
    private String ordersAddress;

    @NotNull(message = "ordersReceiverName cannot be null")
    @ApiModelProperty(required = true)
    private String ordersReceiverName;

    @NotNull(message = "ordersReceiverPhone cannot be null")
    @ApiModelProperty(required = true)
    private String ordersReceiverPhone;

    @NotNull(message = "paymentMethod cannot be null")
    @ApiModelProperty(required = true)
    private Integer paymentMethod;

}
