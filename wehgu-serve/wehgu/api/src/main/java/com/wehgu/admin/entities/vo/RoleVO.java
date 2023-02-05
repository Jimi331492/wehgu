package com.wehgu.admin.entities.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO  extends BaseVO{

    /**
     * 角色UID
     */
    @ApiModelProperty("角色UID")
    private String roleUuid;
    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;



}
