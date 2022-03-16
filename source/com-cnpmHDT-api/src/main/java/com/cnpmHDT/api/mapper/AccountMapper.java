package com.cnpmHDT.api.mapper;

import com.cnpmHDT.api.dto.account.AccountAdminDto;
import com.cnpmHDT.api.dto.account.AccountDto;
import com.cnpmHDT.api.form.account.CreateAccountAdminForm;
import com.cnpmHDT.api.form.account.UpdateAccountAdminForm;
import com.cnpmHDT.api.form.account.UpdateProfileAdminForm;
import com.cnpmHDT.api.storage.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    Account fromCreateAccountAdminFormToAdmin(CreateAccountAdminForm createAccountAdminForm);


    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    void mappingFormUpdateAdminToEntity(UpdateAccountAdminForm updateAccountAdminForm,@MappingTarget Account account);

    @Mapping(source = "fullName", target = "fullName")
    void mappingFormUpdateProfileToEntity(UpdateProfileAdminForm updateProfileAdminForm, @MappingTarget Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "phone", target = "phone")
    AccountDto fromEntityToAccountDto(Account account);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "adminGetMapping")
    List<AccountAdminDto> fromEntityListToDtoList(List<Account> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminAutoCompleteMapping")
    AccountAdminDto fromEntityToAccountAdminDtoAutoComplete(Account account);
}
