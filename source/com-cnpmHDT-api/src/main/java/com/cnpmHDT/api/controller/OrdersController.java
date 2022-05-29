package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.orders.OrdersDto;
import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.form.orders.CreateOrdersForm;
import com.cnpmHDT.api.form.orders.UpdateOrdersForm;
import com.cnpmHDT.api.form.ordersdetail.CreateOrdersDetailForm;
import com.cnpmHDT.api.form.ordersdetail.UpdateOrdersDetailForm;
import com.cnpmHDT.api.mapper.OrdersDetailMapper;
import com.cnpmHDT.api.mapper.OrdersMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.storage.criteria.OrdersCriteria;
import com.cnpmHDT.api.storage.model.Customer;
import com.cnpmHDT.api.storage.model.Orders;
import com.cnpmHDT.api.storage.model.OrdersDetail;
import com.cnpmHDT.api.storage.model.Product;
import com.cnpmHDT.api.storage.repository.*;
import com.cnpmHDT.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static java.lang.Double.*;

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersController extends ABasicController{

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrdersDetailMapper ordersDetailMapper;

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

    private String generateCode() {
        String code = StringUtils.generateRandomString(8);
        code = code.replace("0", "A");
        code = code.replace("O", "Z");
        return code;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders detail.");
        }
        result.setData(ordersMapper.fromEntityToAdminDto(orders));
        result.setMessage("Get orders detail success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrdersForm createOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        Double totalMoney = 0d;
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(createOrdersForm.getCustomerId()).orElse(null);

        if (customer == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Not found customer.");
        }
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        orders.setCode(generateCode());
        ordersRepository.save(orders);

        for(CreateOrdersDetailForm createOrdersDetailForm : createOrdersForm.getCreateOrdersDetailFormList()){
            Product product = productRepository.findById(createOrdersDetailForm.getProductId()).orElse(null);
            if (product == null || !product.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE))
            {
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Not found product.");
            }
            OrdersDetail ordersDetail = ordersDetailMapper.fromCreateOrdersDetailFormToEntity(createOrdersDetailForm);

            double saleOffProduct= (double) product.getSaleoff().intValue();
            ordersDetail.setPrice(product.getPrice() * ((100-saleOffProduct)/100) * ordersDetail.getAmount());
            totalMoney = totalMoney + ordersDetail.getPrice();

            ordersDetail.setOrders(orders);
            ordersDetailRepository.save(ordersDetail);
        }
        orders.setState(cnpmHDTConstant.ORDERS_STATE_PENDING);
        orders.setVat(10);
        double saleOffOrders= (double) orders.getSaleOff().intValue();
        double vatOrders= (double) orders.getVat().intValue();
        orders.setTotalMoney(totalMoney*((100-saleOffOrders)/100)*((100+vatOrders)/100));
        ordersRepository.save(orders);

        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrdersForm updateOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }

        Double totalMoney = 0d;

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Orders orders = ordersRepository.findById(updateOrdersForm.getOrdersId()).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders.");
        }
        for(UpdateOrdersDetailForm updateOrdersDetailForm : updateOrdersForm.getUpdateOrdersDetailFormList()){
            OrdersDetail ordersDetail = ordersDetailRepository.findById(updateOrdersDetailForm.getOrdersDetailId()).orElse(null);
            Product product=ordersDetail.getProduct();
            // || !ordersDetail.getOrders().equals(orders)
            if(ordersDetail == null) {
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Not found orders detail.");
            }
            ordersDetailMapper.fromUpdateOrdersDetailFormToEntity(updateOrdersDetailForm,ordersDetail);
            double saleOffProduct= (double) product.getSaleoff().intValue();
            ordersDetail.setPrice(product.getPrice() * ((100-saleOffProduct)/100) * ordersDetail.getAmount());
            totalMoney = totalMoney + ordersDetail.getPrice();

            ordersDetailRepository.save(ordersDetail);
        }
        ordersMapper.fromUpdateOrdersFormToEntity(updateOrdersForm,orders);
        double saleOffOrders= (double) orders.getSaleOff().intValue();
        double vatOrders= (double) orders.getVat().intValue();
        orders.setTotalMoney(totalMoney*((100-saleOffOrders)/100)*((100+vatOrders)/100));
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders success");
        return apiMessageDto;
    }
    @GetMapping(value = "/detail-by-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDetailDto>> getOrdersDetailByOrdersId(@PathVariable("id") Long id) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get list ordersdetail type.");
        }
        ApiMessageDto<ResponseListObj<OrdersDetailDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        List<OrdersDetail> listOrdersDetail = ordersDetailRepository.findAllByOrders(ordersRepository.findById(id).orElse(null));
        ResponseListObj<OrdersDetailDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(ordersDetailMapper.fromEntityListToOrdersDetailDtoList(listOrdersDetail));
        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list ordersdetail type success");

        return responseListObjApiMessageDto;
    }





}
