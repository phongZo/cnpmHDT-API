package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.orders.OrdersDto;
import com.cnpmHDT.api.form.orders.CreateOrdersForm;
import com.cnpmHDT.api.form.orders.UpdateOrdersForm;
import com.cnpmHDT.api.storage.model.Orders;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersMapper {
    @Mapping(source = "ordersSaleOff", target = "saleOff")
    @Mapping(source = "ordersAddress", target = "address")
    @Mapping(source = "ordersReceiverName", target = "receiverName")
    @Mapping(source = "ordersReceiverPhone", target = "receiverPhone")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Orders fromCreateOrdersFormToEntity(CreateOrdersForm createOrdersForm);

    @Mapping(source = "ordersId", target = "id")
    @Mapping(source = "ordersAddress", target = "address")
    @Mapping(source = "ordersReceiverName", target = "receiverName")
    @Mapping(source = "ordersReceiverPhone", target = "receiverPhone")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateOrdersFormToEntity(UpdateOrdersForm updateOrdersForm, @MappingTarget Orders orders);

    @Mapping(source = "id", target = "ordersId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "totalMoney", target = "ordersTotalMoney")
    @Mapping(source = "state", target = "ordersState")
    @Mapping(source = "vat", target = "ordersVat")
    @Mapping(source = "address", target = "ordersAddress")
    @Mapping(source = "receiverName", target = "ordersReceiverName")
    @Mapping(source = "receiverPhone", target = "ordersReceiverPhone")
    @Mapping(source = "code", target = "ordersCode")
    @Mapping(source = "paymentMethod", target = "ordersPaymentMethod")
    @Mapping(source = "saleOff", target = "ordersSaleOff")
    @Mapping(source = "createdDate", target = "createdDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDto fromEntityToAdminDto(Orders orders);

    @IterableMapping(elementTargetType = OrdersDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDto> fromEntityListToOrdersDtoList(List<Orders> orders);

    @Mapping(source = "id", target = "ordersId")
    @Mapping(source = "saleOff", target = "ordersSaleOff")
    @Mapping(source = "totalMoney", target = "ordersTotalMoney")
    @Mapping(source = "state", target = "ordersState")
    @Mapping(source = "address", target = "ordersAddress")
    @Mapping(source = "code", target = "ordersCode")
    @Mapping(source = "receiverName", target = "ordersReceiverName")
    @Mapping(source = "paymentMethod", target = "ordersPaymentMethod")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    OrdersDto fromEntityToClientOrdersDto(Orders orders);
}
