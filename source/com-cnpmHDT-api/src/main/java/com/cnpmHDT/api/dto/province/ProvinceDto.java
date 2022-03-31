package com.cnpmHDT.api.dto.province;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import com.cnpmHDT.api.storage.model.Province;
import lombok.Data;

@Data
public class ProvinceDto extends ABasicAdminDto {
    private String name;
    private String provinceName;
    private Integer parentId;
    private Integer provinceKind;
    private Integer status;
}
