package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AuthorizeDTO {

    @ApiModelProperty("被授权的角色UID")
    private String roleUID;

    @ApiModelProperty("权限UID列表")
    private List<String> permUIDList;
}
