package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.customer.CustomerDto;
import com.cnpmHDT.api.dto.province.ProvinceDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.form.customer.CreateCustomerForm;
import com.cnpmHDT.api.form.customer.UpdateCustomerForm;
import com.cnpmHDT.api.form.province.CreateProvinceForm;
import com.cnpmHDT.api.form.province.UpdateProvinceForm;
import com.cnpmHDT.api.mapper.ProvinceMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.storage.criteria.CustomerCriteria;
import com.cnpmHDT.api.storage.criteria.ProvinceCriteria;
import com.cnpmHDT.api.storage.model.Category;
import com.cnpmHDT.api.storage.model.Customer;
import com.cnpmHDT.api.storage.model.Group;
import com.cnpmHDT.api.storage.model.Province;
import com.cnpmHDT.api.storage.repository.AccountRepository;
import com.cnpmHDT.api.storage.repository.GroupRepository;
import com.cnpmHDT.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/v1/province")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProvinceController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    com.cnpmHDT.api.service.cnpmHDTApiService cnpmHDTApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> list(ProvinceCriteria provinceCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Province> listProvince = provinceRepository.findAll(provinceCriteria.getSpecification(), pageable);
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoList(listProvince.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listProvince.getTotalPages());
        responseListObj.setTotalElements(listProvince.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list province success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProvinceDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province.");
        }
        result.setData(provinceMapper.fromEntityToAdminDto(province));
        result.setMessage("Get province success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProvinceForm createProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        // Map the data from CreateProvinceForm into Entity province
        Province province = provinceMapper.fromCreateProvinceFormToEntity(createProvinceForm);

        if(createProvinceForm.getParentId() != null) {
            Province parentProvince = provinceRepository.findById(createProvinceForm.getParentId()).orElse(null);
            if (parentProvince == null || !parentProvince.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)) {
                throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province parent");
            }
            //province.setParentProvince(parentProvince);
            if(!createProvinceForm.getProvinceKind().equals(parentProvince.getKind() + 1)){
                throw new RequestException(ErrorCode.PROVINCE_ERROR_BAD_REQUEST, "Kind of province invalid");
            }
            province.setParentProvince(parentProvince);
        }
        else if(createProvinceForm.getParentId() == null){
            if (!createProvinceForm.getProvinceKind().equals(cnpmHDTConstant.PROVINCE_KIND_PROVINCE)) {
                throw new RequestException(ErrorCode.PROVINCE_ERROR_BAD_REQUEST, "Kind of province invalid");
            }
        }

        // repository provide save function to save the data into database /*|| parentProvinceKind.getKind()-province.getKind().longValue()>1/*
        provinceRepository.save(province);
        apiMessageDto.setMessage("Create province success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProvinceForm updateProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Province province = provinceRepository.findById(updateProvinceForm.getId()).orElse(null);
        if(province == null || !province.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province.");
        }
        
        // Map data from UpdateProvince AdminForm to province entity
        provinceMapper.fromUpdateProvinceFormToEntity (updateProvinceForm, province);

        //save province
        provinceRepository.save(province);
        apiMessageDto.setMessage("Update province success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<ProvinceDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province");
        }

        provinceRepository.delete(province);
        result.setMessage("Delete province success");
        return result;
    }


}
