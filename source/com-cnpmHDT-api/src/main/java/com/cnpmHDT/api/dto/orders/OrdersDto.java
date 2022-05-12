package com.cnpmHDT.api.dto.orders;

import com.cnpmHDT.api.dto.ABasicAdminDto;

import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.storage.model.OrdersDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends ABasicAdminDto {
    private Long ordersId;
    private Long customerId;
    private Integer ordersSaleOff;
    private Double ordersTotalMoney;
    private Integer ordersVat;
    private Integer ordersState;
    private Integer ordersPrevState;
    private String ordersAddress; // Dia chi giao hang
    private String ordersReceiverName; // Ten nguoi nhan
    private String ordersReceiverPhone; // Sdt nguoi nhan
    private String ordersCode; // Random 6 chữ
    private Integer ordersPaymentMethod; // Phương thức thanh toán: 1: COD, 2: Online

    private List<OrdersDetailDto> ordersDetailDtoList;
}
