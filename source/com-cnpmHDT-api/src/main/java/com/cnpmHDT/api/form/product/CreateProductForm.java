package com.cnpmHDT.api.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProductForm {

    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String productName;

    @NotNull(message = "productPrice cannot be null")
    @ApiModelProperty(required = true)
    private Double productPrice;

    @NotEmpty(message = "productImage cannot be null")
    @ApiModelProperty(required = true)
    private String productImage;

    @NotEmpty(message = "productDescription cannot be null")
    @ApiModelProperty(required = true)
    private String productDescription;

    @NotEmpty(message = "productShortDescription cannot be null")
    @ApiModelProperty(required = true)
    private String productShortDescription;

    @NotNull(message = "productCategoryId cannot be null")
    @ApiModelProperty(required = true)
    private Long productCategoryId;

    @ApiModelProperty(name = "productSaleOff")
    private Integer productSaleOff;
}
