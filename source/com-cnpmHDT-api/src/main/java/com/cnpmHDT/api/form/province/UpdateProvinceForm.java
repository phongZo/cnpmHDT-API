package com.cnpmHDT.api.form.province;

import com.cnpmHDT.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProvinceForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotEmpty(message = "name cannot be null")  //This field can not be empty (for String)
    @ApiModelProperty (required = true)                     // display that this field is required
    private String name;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty (required = true)
    private Integer status;
}
