package com.cnpmHDT.api.dto.ordersdetail;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {
    private Long ordersDetailId;
    private Double ordersDetailPrice;
    private Integer ordersDetailAmount;
    private Long productId;
    private String productName;
    private Long ordersId;
}
