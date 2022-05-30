package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.orders.OrdersDto;
import com.cnpmHDT.api.dto.ordersdetail.OrdersDetailDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.form.orders.ClientCancelOrdersForm;
import com.cnpmHDT.api.form.orders.CreateOrdersForm;
import com.cnpmHDT.api.form.orders.UpdateOrdersForm;
import com.cnpmHDT.api.form.orders.UpdateStateOrdersForm;
import com.cnpmHDT.api.mapper.OrdersDetailMapper;
import com.cnpmHDT.api.mapper.OrdersMapper;
import com.cnpmHDT.api.storage.criteria.OrdersCriteria;
import com.cnpmHDT.api.storage.model.*;
import com.cnpmHDT.api.storage.repository.*;
import com.cnpmHDT.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    AccountRepository accountRepository;

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
    @Transactional
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrdersForm createOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<OrdersDetail> ordersDetailList = ordersDetailMapper
                .fromCreateOrdersDetailFormListToOrdersDetailList(createOrdersForm.getCreateOrdersDetailFormList());
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        setCustomerCreateForm(orders,createOrdersForm);
        orders.setCode(generateCode());
        orders.setState(cnpmHDTConstant.ORDERS_STATE_CREATED);
        Orders savedOrder = ordersRepository.save(orders);
        /*-----------------------Xử lý orders detail------------------ */
        amountPriceCal(orders,ordersDetailList,savedOrder);  //Tổng tiền hóa đơn
        ordersDetailRepository.saveAll(ordersDetailList);
        /*-----------------------Quay lại xử lý orders------------------ */

        ordersRepository.save(orders);
        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }

    private void setCustomerCreateForm(Orders orders, CreateOrdersForm createOrdersForm) {
        Customer customerCheck = customerRepository.findCustomerByAccountPhone(createOrdersForm.getOrdersReceiverPhone());
        if (customerCheck == null) {
            Account savedAccount = new Account();
            savedAccount.setPhone(createOrdersForm.getOrdersReceiverPhone());
            savedAccount.setUsername(createOrdersForm.getOrdersReceiverPhone());
            savedAccount.setFullName(createOrdersForm.getOrdersReceiverName());
            savedAccount.setKind(cnpmHDTConstant.USER_KIND_CUSTOMER);

            Customer savedCustomer = new Customer();
            savedCustomer.setAccount(savedAccount);
            savedCustomer.setAddress(createOrdersForm.getOrdersAddress());
            savedCustomer = customerRepository.save(savedCustomer);
            orders.setCustomer(savedCustomer);
        }
        else{
            orders.setCustomer(customerCheck);
        }
    }

    @PutMapping(value = "/update-state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStateOrdersForm updateStateOrdersForm, BindingResult bindingResult) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateStateOrdersForm.getId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Orders Not Found");
        }
        checkNewState(updateStateOrdersForm,orders);
        Integer prevState = orders.getState();
        orders.setState(updateStateOrdersForm.getState());
        orders.setPrevState(prevState);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders state success");
        return apiMessageDto;
    }

    private void checkNewState(UpdateStateOrdersForm updateStateOrdersForm,Orders orders) {
        // state mới phải lớn hơn state cũ
        if((updateStateOrdersForm.getState() <= orders.getState())){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Update orders state must mor than or equal old state");
        }
        // State 3 4 không thể update
        Integer state = orders.getState();
        if(state.equals(cnpmHDTConstant.ORDERS_STATE_DONE) || state.equals(cnpmHDTConstant.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not update orders in state 3 or 4");
        }
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrdersForm updateOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        // Lúc này orders sẽ là orders đang trong database (orders cũ)
        Orders orders = ordersRepository.findById(updateOrdersForm.getOrdersId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Orders Not Found");
        }
        if(!orders.getState().equals(cnpmHDTConstant.ORDERS_STATE_CREATED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not update info in this state");
        }
        setCustomerUpdateForm(updateOrdersForm, orders);
        //Map update form vô orders --> orders lúc này là orders mới với thông tin mới
        ordersMapper.fromUpdateOrdersFormToEntity(updateOrdersForm,orders);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders success");
        return apiMessageDto;
    }




    private void setCustomerUpdateForm(UpdateOrdersForm updateOrdersForm, Orders orders) {
        Customer customerCheck = customerRepository.findCustomerByAccountPhone(updateOrdersForm.getOrdersReceiverPhone());
        if (customerCheck == null) {
            {
                Account savedAccount = new Account();
                savedAccount.setPhone(updateOrdersForm.getOrdersReceiverPhone());
                savedAccount.setUsername(updateOrdersForm.getOrdersReceiverPhone());
                savedAccount.setFullName(updateOrdersForm.getOrdersReceiverName());
                savedAccount.setKind(cnpmHDTConstant.USER_KIND_CUSTOMER);

                Customer savedCustomer = new Customer();
                savedCustomer.setAccount(savedAccount);
                savedCustomer.setAddress(updateOrdersForm.getOrdersAddress());
                savedCustomer = customerRepository.save(savedCustomer);
                orders.setCustomer(savedCustomer);
            }
        }
        else{
            orders.setCustomer(customerCheck);
        }
    }


    @GetMapping(value = "/client-list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> clientList(OrdersCriteria ordersCriteria, Pageable pageable) {
        if (!isCustomer()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Customer customer = getCurrentCustomer();
        ordersCriteria.setCustomerId(customer.getId());
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

    @PostMapping(value = "/client-create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody CreateOrdersForm createOrdersForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<OrdersDetail> ordersDetailList = ordersDetailMapper
                .fromCreateOrdersDetailFormListToOrdersDetailList(createOrdersForm.getCreateOrdersDetailFormList());
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        setCustomerClient(orders);
        orders.setCode(generateCode());
        orders.setState(cnpmHDTConstant.ORDERS_STATE_CREATED);
        Orders savedOrder = ordersRepository.save(orders);
        /*-----------------------Xử lý orders detail------------------ */
        amountPriceCal(orders,ordersDetailList,savedOrder);  //Tổng tiền hóa đơn
        ordersDetailRepository.saveAll(ordersDetailList);
        /*-----------------------Quay lại xử lý orders------------------ */

        ordersRepository.save(orders);
        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }

    private void amountPriceCal(Orders orders,List<OrdersDetail> ordersDetailList, Orders savedOrder) {
        int checkIndex = 0;
        Double amountPrice = 0.0;
        for (OrdersDetail ordersDetail : ordersDetailList){
            Product productCheck = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
            if (productCheck == null){
                throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "product in index "+checkIndex+"is not existed");
            }
            Double productPrice = productCheck.getPrice();
            Double priceProductAfterSale = productPrice - (productPrice * productCheck.getSaleoff() / 100);
            ordersDetail.setPrice(priceProductAfterSale);
            amountPrice = amountPrice + priceProductAfterSale * (ordersDetail.getAmount()); // Tổng tiền hóa đơn
            ordersDetail.setOrders(savedOrder);
            checkIndex++;
        }
        Double totalMoney = totalMoneyHaveToPay(amountPrice,orders);
        orders.setTotalMoney(totalMoney);
        orders.setVat(cnpmHDTConstant.ORDER_VAT);
    }

    private Double totalMoneyHaveToPay(Double amountPrice,Orders orders) {
        double saleOff = (double)orders.getSaleOff() / 100;             // Tính saleOff (%)
        Double amountAfterSaleOff =  amountPrice - amountPrice * saleOff;                  // Tổng tiền sau khi trừ saleOff
        double VAT = (double)cnpmHDTConstant.ORDER_VAT / 100;             // Tính VAT (%)
        amountPrice = amountAfterSaleOff + amountAfterSaleOff * VAT ;              // Tiền sau cùng bằng tiền sau khi saleOff cộng với VAT (10% tổng tiền)
        return Math.round(amountPrice * 100.0) / 100.0;          // Làm tròn đến thập phân thứ 2
    }


    private void setCustomerClient(Orders orders) {
        Long id = getCurrentUserId();
        Customer customerCheck = customerRepository.findCustomerByAccountId(id);
        if (customerCheck == null || !customerCheck.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found current customer");
        }
        orders.setCustomer(customerCheck);
    }

    @PutMapping(value = "/client-cancel-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientCancelOrders(@Valid @RequestBody ClientCancelOrdersForm clientCancelOrdersForm, BindingResult bindingResult) {
        if (!isCustomer()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to cancel orders.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(clientCancelOrdersForm.getOrdersId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Order Not Found");
        }
        checkState(orders);
        Integer prevState = orders.getState();
        orders.setState(cnpmHDTConstant.ORDERS_STATE_CANCELED);
        orders.setPrevState(prevState);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Cancel order success");
        return apiMessageDto;
    }

    private void checkState(Orders orders) {
        Integer state = orders.getState();
        if(state.equals(cnpmHDTConstant.ORDERS_STATE_DONE) || state.equals(cnpmHDTConstant.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not cancel order in this state");
        }
    }


    @GetMapping(value = "/client-get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> clientGet(@PathVariable("id") Long id) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();
        Customer customer = getCurrentCustomer();
        Orders orders = ordersRepository.findOrdersByIdAndCustomerId(id,customer.getId());
        if(orders == null || !orders.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders.");
        }
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper.fromEntityListToOrdersDetailClientDtoList(ordersDetailRepository.findAllByOrders(orders));
        OrdersDto ordersDto = ordersMapper.fromEntityToClientOrdersDto(orders);
        ordersDto.setOrdersDetailDtoList(ordersDetailDtoList);
        result.setData(ordersDto);
        result.setMessage("Get orders success");
        return result;
    }


    private Customer getCurrentCustomer() {
        Long userId = getCurrentUserId();
        Customer customer = customerRepository.findCustomerByAccountId(userId);
        if(customer == null || !customer.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found current customer.");
        }
        return customer;
    }
}
