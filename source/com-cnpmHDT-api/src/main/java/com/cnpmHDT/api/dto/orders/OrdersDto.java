package com.cnpmHDT.api.dto.orders;

import com.cnpmHDT.api.dto.ABasicAdminDto;
<<<<<<< Updated upstream

public class Orders extends ABasicAdminDto {
=======
import lombok.Data;

@Data
public class OrdersDto extends ABasicAdminDto {
>>>>>>> Stashed changes
    private Long ordersId;
}
