package com.cnpmHDT.api.form.customer;

import com.cnpmHDT.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateCustomerForm {
    @ApiModelProperty(name = "customerEmail")               // Name of this field in form con swagger)
    private String customerEmail;

    @NotEmpty(message = "customerFullName cannot be null")  //This field can not be empty (for String)
    @ApiModelProperty (required = true)                     // display that this field is required
    private String customerFullName;

    @NotEmpty(message = "customerUserName cannot be null")  //This field can not be empty (for String)
    @ApiModelProperty (required = true)                     // display that this field is required
    private String customerUsername;

    @ApiModelProperty(name = "customer Password")
    private String customerPassword;

    @NotEmpty(message = "customer Phone cannot be null")
    @ApiModelProperty(required = true)
    private String customerPhone;

    @ApiModelProperty(name = "customerAddress")
    private String customerAddress;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty (required = true)
    private Integer status;

    @NotNull(message = "birthday cannot be null")           //This field can not be null (for Integer, Date...)
    @ApiModelProperty (required = true)
    private Date birthday;

    @NotNull(message = "gender cannot be null")
    @ApiModelProperty (required = true)
    private Integer gender;

    @NotNull(message = "isLoyalty cannot be null")
    @ApiModelProperty (required = true)
    private Boolean isLoyalty;
    private Integer loyaltyLevel;
    private Integer saleOff;

}
