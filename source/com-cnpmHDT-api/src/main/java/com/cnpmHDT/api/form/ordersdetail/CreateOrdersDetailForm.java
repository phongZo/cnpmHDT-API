<<<<<<< Updated upstream
package com.cnpmHDT.api.form.ordersdetail;public class CreateOrdersDetailForm {
=======
package com.cnpmHDT.api.form.ordersdetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateOrdersDetailForm {
    @NotNull(message = "ordersDetailPrice cannot be null")
    @ApiModelProperty(required = true)
    private Double ordersDetailPrice;

    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @NotNull(message = "ordersId cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;

    @NotNull(message = "ordersDetailAmount cannot be null")
    @ApiModelProperty(required = true)
    private Integer ordersDetailAmount;
>>>>>>> Stashed changes
}
