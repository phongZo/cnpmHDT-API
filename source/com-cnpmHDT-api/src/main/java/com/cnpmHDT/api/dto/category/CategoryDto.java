package com.cnpmHDT.api.dto.category;

import com.cnpmHDT.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class CategoryDto extends ABasicAdminDto {
    private String categoryName;
    private String categoryDescription;
    private String categoryImage;
    private Integer categoryOrdering;
    private Integer categoryKind;
    private Integer parentId;
}
