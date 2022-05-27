package com.cnpmHDT.api.form.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CustomerRegisterForm {
    @ApiModelProperty(required = true)
    @NotEmpty(message = "customerPhone cannot be null")
    private String customerPhone;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "customerFullName cannot be null")
    private String customerFullName;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "customerUsername cannot be null")
    private String customerUsername;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "customerPassword cannot be null")
    private String customerPassword;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "address cannot be null")
    private String address;
}
