package com.cnpmHDT.api.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String productName;

    @NotNull(message = "productPrice cannot be null")
    @ApiModelProperty(required = true)
    private Double productPrice;

    @NotEmpty(message = "productImage cannot be null")
    @ApiModelProperty(required = true)
    private String productImage;

    @ApiModelProperty(name = "productSaleOff")
    private Integer productSaleOff;
}
