package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.category.CategoryDto;
import com.cnpmHDT.api.dto.product.ProductDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.form.product.CreateProductForm;
import com.cnpmHDT.api.form.product.UpdateProductForm;
import com.cnpmHDT.api.mapper.ProductMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.storage.criteria.ProductCriteria;
import com.cnpmHDT.api.storage.model.Category;
import com.cnpmHDT.api.storage.model.Product;
import com.cnpmHDT.api.storage.repository.CategoryRepository;
import com.cnpmHDT.api.storage.repository.GroupRepository;
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
import java.util.List;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductController extends ABasicController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductMapper productMapper ;

    @Autowired
    cnpmHDTApiService cnpmHDTApiService;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> list(ProductCriteria productCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<ProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> listProduct = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListObj<ProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoList(listProduct.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listProduct.getTotalPages());
        responseListObj.setTotalElements(listProduct.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }


    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ProductDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product.");
        }
        result.setData(productMapper.fromEntityToAdminDto(product));
        result.setMessage("Get product success");
        return result;
    }

    @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> clientList(ProductCriteria productCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListObj<ProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> productList = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListObj<ProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductClientDtoList(productList.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(productList.getTotalPages());
        responseListObj.setTotalElements(productList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> clientGet(@PathVariable("id") Long id) {
        ApiMessageDto<ProductDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product.");
        }
        result.setData(productMapper.fromEntityToClientDtoDetailProduct(product));
        result.setMessage("Get product success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Category category=categoryRepository.findById(createProductForm.getProductCategoryId()).orElse(null);
        if(category==null || !category.getStatus().equals(cnpmHDTConstant.STATUS_ACTIVE) )
        {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_BAD_REQUEST, "Not found product.");
        }
        Product product = productMapper.fromCreateProductFormToEntity(createProductForm);
        productRepository.save(product);
        apiMessageDto.setMessage("Create product success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productRepository.findById(updateProductForm.getProductId()).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product.");
        }

        if(StringUtils.isNoneBlank(product.getImage()) && !updateProductForm.getProductImage().equals(product.getImage())) {
            cnpmHDTApiService.deleteFile(product.getImage());
        }
        productMapper.fromUpdateProductFormToEntity(updateProductForm, product);
        productRepository.save(product);
        apiMessageDto.setMessage("Update product success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CategoryDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CategoryDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product");
        }
        cnpmHDTApiService.deleteFile(product.getImage());
        productRepository.delete(product);
        result.setMessage("Delete product success");
        return result;
    }

    @GetMapping(value = "/products-by-category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> getProductByCategoryId(@PathVariable("categoryId") Long categoryId) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed get list list product type.");
        }
        ApiMessageDto<ResponseListObj<ProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        List<Product> listProduct = productRepository.findAllByCategory(categoryRepository.findById(categoryId).orElse(null));
        ResponseListObj<ProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoList(listProduct));
        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list product type success");

        return responseListObjApiMessageDto;
    }
}
