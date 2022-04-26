package com.cnpmHDT.api.form.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Data
public class UpdateCustomerProfileForm {
    @ApiModelProperty(name = "phoneOrEmail")
    @Email
    private String customerEmail;

    @ApiModelProperty(name = "customerFullName",required = true)
    @NotEmpty(message = "customerFullName cannot be null")
    private String customerFullName;

    @ApiModelProperty(name = "address",required = true)
    @NotEmpty(message = "address cannot be null")
    private String address;

//    @ApiModelProperty(name = "customerPassword")
//    private String customerPassword;
//
//    @ApiModelProperty(name = "oldPassword",required = true)
//    @NotEmpty(message = "oldPassword cannot be null")
//    private String oldPassword;

    @ApiModelProperty(name = "birthday")
    private Date birthday;

    @ApiModelProperty(name = "sex")
    private Integer gender;
}
