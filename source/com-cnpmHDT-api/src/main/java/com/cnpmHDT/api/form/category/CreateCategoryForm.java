package com.cnpmHDT.api.form.category;

import com.cnpmHDT.api.validation.CategoryKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateCategoryForm {

    @NotEmpty(message = "categoryName cannot be null")
    @ApiModelProperty(required = true)
    private String categoryName;

    @NotEmpty(message = "categoryDescription cannot be null")
    @ApiModelProperty(required = true)
    private String categoryDescription;

    @ApiModelProperty(name = "categoryOrdering")
    private Integer categoryOrdering;

    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
