package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.orders.OrdersDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.mapper.OrdersMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.storage.criteria.OrdersCriteria;
import com.cnpmHDT.api.storage.model.Orders;
import com.cnpmHDT.api.storage.repository.CategoryRepository;
import com.cnpmHDT.api.storage.repository.GroupRepository;
import com.cnpmHDT.api.storage.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersController extends ABasicController{

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrdersMapper ordersMapper ;

    @Autowired
    com.cnpmHDT.api.service.cnpmHDTApiService cnpmHDTApiService;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> list( OrdersCriteria ordersCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode. ORDERS_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Orders> listOrders = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(ordersMapper.fromEntityListToOrdersDtoList(listOrders.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listOrders.getTotalPages());
        responseListObj.setTotalElements(listOrders.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }


}
