package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.form.ordersdetail.CreateOrdersDetailForm;

import com.cnpmHDT.api.form.ordersdetail.UpdateOrdersDetailForm;
import com.cnpmHDT.api.mapper.OrdersDetailMapper;
import com.cnpmHDT.api.storage.criteria.OrdersDetailCriteria;
import com.cnpmHDT.api.storage.model.Orders;
import com.cnpmHDT.api.storage.model.OrdersDetail;
import com.cnpmHDT.api.storage.model.Product;

import com.cnpmHDT.api.storage.repository.GroupRepository;
import com.cnpmHDT.api.storage.repository.OrdersDetailRepository;
import com.cnpmHDT.api.storage.repository.OrdersRepository;
import com.cnpmHDT.api.storage.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/orders-detail")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersDetailController extends ABasicController {
    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailMapper ordersDetailMapper;

    @Autowired
    com.cnpmHDT.api.service.cnpmHDTApiService cnpmHDTApiService;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDetailDto>> list(OrdersDetailCriteria ordersDetailCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<OrdersDetailDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<OrdersDetail> listOrdersDetail = ordersDetailRepository.findAll(ordersDetailCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDetailDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(ordersDetailMapper.fromEntityListToOrdersDetailDtoList(listOrdersDetail.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listOrdersDetail.getTotalPages());
        responseListObj.setTotalElements(listOrdersDetail.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }


    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDetailDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDetailDto> result = new ApiMessageDto<>();

        OrdersDetail ordersDetail = ordersDetailRepository.findById(id).orElse(null);
        if(ordersDetail == null) {
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_NOT_FOUND, "Not found orders detail.");
        }
        result.setData(ordersDetailMapper.fromEntityToAdminDto(ordersDetail));
        result.setMessage("Get orders detail success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrdersDetailForm createOrdersDetailForm,BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product=productRepository.findById(createOrdersDetailForm.getProductId()).orElse(null);
        Orders orders=ordersRepository.findById(createOrdersDetailForm.getOrdersId()).orElse(null);
        if(product==null|| !product.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE) ||
                orders==null || !orders.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE))
        {
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_BAD_REQUEST, "Not found orders detail.");
        }
        OrdersDetail ordersDetail = ordersDetailMapper.fromCreateOrdersDetailFormToEntity(createOrdersDetailForm);
        ordersDetailRepository.save(ordersDetail);
        apiMessageDto.setMessage("Create orders detail success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrdersDetailForm updateOrdersDetailForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        OrdersDetail ordersDetail = ordersDetailRepository.findById(updateOrdersDetailForm.getOrdersDetailId()).orElse(null);
        if(ordersDetail == null) {
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_NOT_FOUND, "Not found orders detail.");
        }

        ordersDetailMapper.fromUpdateOrdersDetailFormToEntity(updateOrdersDetailForm, ordersDetail);
        ordersDetailRepository.save(ordersDetail);
        apiMessageDto.setMessage("Update orders detail success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<OrdersDetailDto>  delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<OrdersDetailDto> result = new ApiMessageDto<>();

        OrdersDetail ordersDetail = ordersDetailRepository.findById(id).orElse(null);
        if(ordersDetail == null) {
            throw new RequestException(ErrorCode.ORDERSDETAIL_ERROR_NOT_FOUND, "Not found orders detail");
        }
        ordersDetailRepository.delete(ordersDetail);
        result.setMessage("Delete orders detail success");
        return result;
    }
}
