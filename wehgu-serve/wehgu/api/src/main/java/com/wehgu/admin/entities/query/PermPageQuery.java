package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PermPageQuery extends BasePageQuery {

    @ApiModelProperty(value = "权限名")
    private String title;

    @ApiModelProperty(value = "权限标识")
    private Integer auth;

    @ApiModelProperty(value = "权限类型")
    private Integer type;

    @ApiModelProperty(value = "角色UID")
    private String roleUuid;
}
