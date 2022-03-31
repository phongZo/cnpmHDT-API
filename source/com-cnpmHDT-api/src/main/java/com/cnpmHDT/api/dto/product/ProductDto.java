package com.cnpmHDT.api.dto.product;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class ProductDto extends ABasicAdminDto {
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImage;
    private String productDescription;
    private Long productCategoryId;
    private String productShortDescription;
    private Integer productSaleOff;

}
