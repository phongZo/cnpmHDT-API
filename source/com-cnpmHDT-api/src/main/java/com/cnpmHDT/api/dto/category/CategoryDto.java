package com.cnpmHDT.api.dto.category;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class CategoryDto extends ABasicAdminDto {
    private String categoryName;
    private String categoryDescription;
    private Integer categoryOrdering;
    private Integer parentId;
}
