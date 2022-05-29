package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.product.ProductDto;
import com.cnpmHDT.api.form.product.CreateProductForm;
import com.cnpmHDT.api.form.product.UpdateProductForm;
import com.cnpmHDT.api.storage.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "productShortDescription", target = "shortDescription")
    @Mapping(source = "productCategoryId", target = "category.id")
    @Mapping(source = "productSaleOff", target = "saleoff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Product fromCreateProductFormToEntity(CreateProductForm createProductForm);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "productSaleOff", target = "saleoff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateProductFormToEntity(UpdateProductForm updateProductForm, @MappingTarget Product product);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "shortDescription", target = "productShortDescription")
    @Mapping(source = "saleoff", target = "productSaleOff")

    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    ProductDto fromEntityToAdminDto(Product product);

    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "adminGetMapping")
    List<ProductDto> fromEntityListToProductDtoList(List<Product> products);


    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "shortDescription", target = "productShortDescription")
    @Mapping(source = "saleoff", target = "productSaleOff")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "price", target = "productPrice")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientDetailProductGetMapping")
    ProductDto fromEntityToClientDtoDetailProduct(Product product);


    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "saleoff", target = "productSaleOff")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "price", target = "productPrice")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    ProductDto fromEntityToClientDto(Product product);

    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "clientGetMapping")
    List<ProductDto> fromEntityListToProductClientDtoList(List<Product> products);
}
