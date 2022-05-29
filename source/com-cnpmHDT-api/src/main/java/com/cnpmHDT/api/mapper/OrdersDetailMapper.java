package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.form.ordersdetail.CreateOrdersDetailForm;
import com.cnpmHDT.api.storage.model.OrdersDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductMapper.class})
public interface OrdersDetailMapper {
    @Mapping(source = "ordersDetailAmount", target = "amount")
    @Mapping(source = "productId", target = "product.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    OrdersDetail fromCreateOrdersDetailFormToEntity(CreateOrdersDetailForm createOrdersDetailForm);

    @IterableMapping(elementTargetType = OrdersDetail.class, qualifiedByName = "adminCreateMapping")
    List<OrdersDetail> fromCreateOrdersDetailFormListToOrdersDetailList(List<CreateOrdersDetailForm> createOrdersDetailFormList);
    

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

    @Mapping(source = "id", target = "ordersDetailId")
    @Mapping(source = "product", target = "productDto", qualifiedByName = "clientDetailProductGetMapping")
    @Mapping(source = "price", target = "ordersDetailPrice")
    @Mapping(source = "amount", target = "ordersDetailAmount")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    OrdersDetailDto fromEntityToOrdersDetailClientDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "clientGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailClientDtoList(List<OrdersDetail> ordersDetailList);
}
