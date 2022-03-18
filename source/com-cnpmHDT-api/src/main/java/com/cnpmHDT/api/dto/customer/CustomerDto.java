package com.cnpmHDT.api.dto.customer;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto extends ABasicAdminDto {
    private String addressCustomer;
    private Date birthdayCustomer;
    private Integer genderCustomer;
    private Boolean isLoyaltyCustomer;
    private Integer loyaltyLevelCustomer;
    private Integer saleOffCustomer;
}
