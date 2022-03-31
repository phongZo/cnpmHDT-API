package com.cnpmHDT.api.mapper;


import com.cnpmHDT.api.dto.province.ProvinceDto;
import com.cnpmHDT.api.form.province.CreateProvinceForm;
import com.cnpmHDT.api.form.province.UpdateProvinceForm;
import com.cnpmHDT.api.storage.model.Province;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProvinceMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "provinceKind", target = "kind")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Province fromCreateProvinceFormToEntity(CreateProvinceForm createProvinceForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateProvinceFormToEntity(UpdateProvinceForm updateProvinceForm, @MappingTarget Province province);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parentProvince.id", target = "parentId")
    @Mapping(source = "parentProvince.name", target = "provinceName")
    @Mapping(source = "kind", target = "provinceKind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    ProvinceDto fromEntityToAdminDto(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "adminGetMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoList(List<Province> provinces);
}
