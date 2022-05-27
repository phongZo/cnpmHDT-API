package com.cnpmHDT.api.controller;


import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.category.CategoryDto;
import com.cnpmHDT.api.dto.customer.CustomerDto;
import com.cnpmHDT.api.exception.RequestException;

import com.cnpmHDT.api.form.customer.CreateCustomerForm;
import com.cnpmHDT.api.form.customer.CustomerRegisterForm;
import com.cnpmHDT.api.form.customer.UpdateCustomerForm;
import com.cnpmHDT.api.form.customer.UpdateCustomerProfileForm;
import com.cnpmHDT.api.mapper.CustomerMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.storage.criteria.CustomerCriteria;

import com.cnpmHDT.api.storage.model.Account;
import com.cnpmHDT.api.storage.model.Customer;
import com.cnpmHDT.api.storage.model.Group;
import com.cnpmHDT.api.storage.model.Province;
import com.cnpmHDT.api.storage.repository.AccountRepository;
import com.cnpmHDT.api.storage.repository.CustomerRepository;
import com.cnpmHDT.api.storage.repository.GroupRepository;
import com.cnpmHDT.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    cnpmHDTApiService cnpmHDTApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerDto>> list(CustomerCriteria customerCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<CustomerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Customer> listCustomer = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        ResponseListObj<CustomerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(customerMapper.fromEntityListToCustomerDtoList(listCustomer.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCustomer.getTotalPages());
        responseListObj.setTotalElements(listCustomer.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();

        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer.");
        }
        result.setData(customerMapper.fromEntityToAdminDto(customer));
        result.setMessage("Get customer success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        // repository provide countAccountByPhone function to count account by phone in createCustomerForm
        Long accountCheck = accountRepository
                .countAccountByPhone(createCustomerForm.getCustomerPhone()); // if have one account , throw error
        if (accountCheck > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXIST, "Phone is existed");
        }
        Group group = groupRepository.findFirstByKind(cnpmHDTConstant.GROUP_KIND_CUSTOMER);
        if (group == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND, "Group does not exist!");
        }
        // Map the data from CreateCustomerForm into Entity Customer
        Customer customer = customerMapper.fromCreateCustomerFormToEntity(createCustomerForm);

        // set the information to create account for this customer
        customer.getAccount().setGroup(group);
        customer.getAccount().setKind(cnpmHDTConstant.USER_KIND_CUSTOMER);
        customer.getAccount().setPassword(passwordEncoder.encode(createCustomerForm.getCustomerPassword()));
        customer.getAccount().setStatus(createCustomerForm.getStatus());

        // repository provide save function to save the data into database
        customerRepository.save(customer);
        apiMessageDto.setMessage("Create customer success");
        return apiMessageDto;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> register(@Valid @RequestBody CustomerRegisterForm customerRegisterForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Account accountCheck = accountRepository.findAccountByUsername(customerRegisterForm.getCustomerUsername());
        if (accountCheck != null){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_BAD_REQUEST, "Phone and username already existed");
        }
        Integer groupKind = cnpmHDTConstant.GROUP_KIND_CUSTOMER;
        Group group = groupRepository.findFirstByKind(groupKind);
        if (group == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_BAD_REQUEST, "Group does not exist!");
        }
        Customer customer = customerMapper.fromCustomerRegisterFormToEntity(customerRegisterForm);
        customer.getAccount().setGroup(group);
        customer.getAccount().setPassword(passwordEncoder.encode(customerRegisterForm.getCustomerPassword()));
        customer.getAccount().setKind(cnpmHDTConstant.USER_KIND_CUSTOMER);
        customerRepository.save(customer);

        apiMessageDto.setMessage("Register account success");
        return apiMessageDto;
    }


    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> getProfile() {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();
        Long id = getCurrentUserId();
        Customer customer = customerRepository.findCustomerByAccountId(id);
        if(customer == null || !customer.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer.");
        }
        result.setData(customerMapper.fromEntityToCustomerProfileDto(customer));
        result.setMessage("Get customer success");
        return result;
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateCustomerProfileForm updateCustomerProfileForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer.");
        }
        customerMapper.fromUpdateCustomerProfileFormToEntity(updateCustomerProfileForm, customer);
        customerRepository.save(customer);
        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;
    }


    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(updateCustomerForm.getId()).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not found customer.");
        }
        // Map data from UpdateCustomer AdminForm to customer entity
        customerMapper.fromUpdateCustomerFormToEntity (updateCustomerForm, customer);

        // if need update loyalty to false --> set loyalDate, LoyalLevel to null , saleoff to O
        if (updateCustomerForm.getIsLoyalty().equals(Boolean.FALSE)){
            customer.setLoyaltyLevel(null);
            customer.setSaleOff(0);
        }

        // if field password in form not blank --> set the password
        if (StringUtils.isNoneBlank (updateCustomerForm.getCustomerPassword())) {
            customer.getAccount().setPassword(passwordEncoder.encode(updateCustomerForm.getCustomerPassword()));
        }


        customer.getAccount().setStatus(updateCustomerForm.getStatus());
        // save account
        accountRepository.save(customer.getAccount());
        //save customer
        customerRepository.save(customer);
        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;
    }



    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CustomerDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();

        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer");
        }

        customerRepository.delete(customer);
        result.setMessage("Delete customer success");
        return result;
    }
}
