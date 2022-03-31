package com.cnpmHDT.api.form.customer;

import com.cnpmHDT.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateCustomerForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true) private Long id; // find the customer need update by id

    @ApiModelProperty (name = "customerEmail")          //Name of this field in form (on swagger)
    private String customerEmail;

    @NotEmpty(message = "customerFullName cannot be null")
    @ApiModelProperty (required = true)
    private String customerFullName;

    @ApiModelProperty(name = "customer Password")
    private String customerPassword;                    // if need change the password, fill this field,

    @ApiModelProperty(name = "customerAddress")
    private String customerAddress;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty (required = true)
    @Status
    private Integer status; // update the status of customer

    @NotNull(message = "birthday cannot be null")
    @ApiModelProperty (required = true)
    private Date birthday;

    private Boolean isLoyalty;
    private Integer loyaltyLevel;
    private Integer saleOff;

}
