package com.cnpmHDT.api.form.province;

import com.cnpmHDT.api.storage.model.Province;
import com.cnpmHDT.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProvinceForm {

    @NotEmpty(message = "Province Name cannot be null")  //This field can not be empty (for String)
    @ApiModelProperty (required = true)                     // display that this field is required
    private String name;

    @NotNull(message = "Kind of province cannot be null")
    @ApiModelProperty(required = true)
    private Integer provinceKind;

    @ApiModelProperty (name = "parentId")
    private Long parentId;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty (required = true)
    private Integer status;

}
