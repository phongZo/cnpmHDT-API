package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.category.CategoryDto;
import com.cnpmHDT.api.dto.customer.CustomerDto;
import com.cnpmHDT.api.form.category.CreateCategoryForm;
import com.cnpmHDT.api.form.category.UpdateCategoryForm;
import com.cnpmHDT.api.form.customer.CreateCustomerForm;
import com.cnpmHDT.api.form.customer.CustomerRegisterForm;
import com.cnpmHDT.api.form.customer.UpdateCustomerForm;
import com.cnpmHDT.api.form.customer.UpdateCustomerProfileForm;
import com.cnpmHDT.api.storage.model.Category;
import com.cnpmHDT.api.storage.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerUsername", target = "account.username")
    @Mapping(source = "customerPhone", target = "account.phone")
    @Mapping(source = "customerAddress", target = "address")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "isLoyalty", target = "isLoyalty")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevel")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Customer fromCreateCustomerFormToEntity(CreateCustomerForm createCustomerForm);

    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerAddress", target = "address")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "isLoyalty", target = "isLoyalty")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevel")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateCustomerFormToEntity(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "gender", target = "genderCustomer")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "address", target = "addressCustomer")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevelCustomer")
    @Mapping(source = "birthday", target = "birthdayCustomer")
    @Mapping(source = "account.username", target = "username")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    CustomerDto fromEntityToCustomerProfileDto(Customer customer);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "address", target = "addressCustomer")
    @Mapping(source = "birthday", target = "birthdayCustomer")
    @Mapping(source = "gender", target = "genderCustomer")
    @Mapping(source = "isLoyalty", target = "isLoyaltyCustomer")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevelCustomer")
    @Mapping(source = "saleOff", target = "saleOffCustomer")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")

    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    CustomerDto fromEntityToAdminDto(Customer customer);

    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "adminGetMapping")
    List<CustomerDto> fromEntityListToCustomerDtoList(List<Customer> customers);

    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "address", target = "address")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientUpdateMapping")
    void fromUpdateCustomerProfileFormToEntity(UpdateCustomerProfileForm updateCustomerProfileForm, @MappingTarget Customer customer);


    @Mapping(source = "customerPhone", target = "account.phone")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerUsername", target = "account.username")
    @Mapping(source = "address", target = "address")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientCreateMapping")
    Customer fromCustomerRegisterFormToEntity(CustomerRegisterForm customerRegisterForm);

}
