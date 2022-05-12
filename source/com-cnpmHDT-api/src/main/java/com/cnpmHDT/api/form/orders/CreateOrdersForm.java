package com.cnpmHDT.api.form.orders;

import com.cnpmHDT.api.form.ordersdetail.CreateOrdersDetailForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrdersForm {

    @NotNull(message = "customerId cannot be null")
    @ApiModelProperty(required = true)
    private Long customerId;

    @ApiModelProperty(name = "ordersSaleOff")
    private Integer ordersSaleOff;

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

    private List<CreateOrdersDetailForm> createOrdersDetailFormList;
}
