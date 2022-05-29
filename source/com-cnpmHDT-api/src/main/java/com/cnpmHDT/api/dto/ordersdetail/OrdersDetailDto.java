package com.cnpmHDT.api.dto.ordersdetail;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import com.cnpmHDT.api.dto.product.ProductDto;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {
    private Long ordersDetailId;
    private Double ordersDetailPrice;
    private Integer ordersDetailAmount;
    private ProductDto productDto;
    private Long productId;
    private String productName;
    private Long ordersId;
}
