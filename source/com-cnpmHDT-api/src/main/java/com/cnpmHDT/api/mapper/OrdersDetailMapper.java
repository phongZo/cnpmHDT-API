package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.form.ordersdetail.CreateOrdersDetailForm;
import com.cnpmHDT.api.form.ordersdetail.UpdateOrdersDetailForm;
import com.cnpmHDT.api.storage.model.OrdersDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersDetailMapper {
    @Mapping(source = "ordersDetailPrice", target = "price")
    @Mapping(source = "ordersDetailAmount", target = "amount")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "ordersId", target = "orders.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    OrdersDetail fromCreateOrdersDetailFormToEntity(CreateOrdersDetailForm createOrdersDetailForm);

    @Mapping(source = "ordersDetailId", target = "id")
    @Mapping(source = "ordersDetailPrice", target = "price")
    @Mapping(source = "ordersDetailAmount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateOrdersDetailFormToEntity(UpdateOrdersDetailForm updateOrdersDetailForm, @MappingTarget OrdersDetail ordersDetail);

    @Mapping(source = "id", target = "ordersDetailId")
    @Mapping(source = "price", target = "ordersDetailPrice")
    @Mapping(source = "amount", target = "ordersDetailAmount")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "orders.id", target = "ordersId")

    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDetailDto fromEntityToAdminDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailDtoList(List<OrdersDetail> ordersDetails);
}
