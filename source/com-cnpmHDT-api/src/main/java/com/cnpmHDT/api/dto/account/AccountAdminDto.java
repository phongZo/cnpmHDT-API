package com.cnpmHDT.api.dto.account;

import com.cnpmHDT.api.dto.group.GroupDto;
import com.cnpmHDT.api.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class AccountAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "username")
    private String username;
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "fullName")
    private String fullName;
    @ApiModelProperty(name = "group")
    private GroupDto group;
    @ApiModelProperty(name = "phone")
    private String phone;
    @ApiModelProperty(name = "salary")
    private Double salary;
}
