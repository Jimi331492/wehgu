package com.wehgu.admin.entities.vo;

import com.wehgu.admin.entities.SysPermission;
import io.swagger.annotations.ApiModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PermVO extends SysPermission{

//    /**
//     * 	系统保留字段，UUID
//     */
//    @ApiModelProperty("系统保留字段，UUID")
//    private String permUuid;
//
//    /**
//     * 上级权限ID
//     */
//    @ApiModelProperty("上级权限名")
//    private String parentTitle;
//
//    /**
//     * 权限名称
//     */
//    @ApiModelProperty("权限名称")
//    private String title;
//
//    /**
//     * 权限标识
//     */
//    @ApiModelProperty("权限标识")
//    private String auth;
//
//    /**
//     * 权限类型 0 菜单/ 1按钮
//     */
//    @ApiModelProperty("权限类型 0 菜单/ 1按钮")
//    private Integer type;
//
//    /**
//     * 菜单的URL
//     */
//    @ApiModelProperty("菜单的URL")
//    private String url;
//
//    /**
//     * 菜单图标
//     */
//    @ApiModelProperty("菜单图标")
//    private String icon;
//
//    /**
//     * 菜单排序
//     */
//    @ApiModelProperty("菜单排序")
//    private Integer orderNum;
//
//    @TableField(exist = false)
//    private List<PermVO> children;


}
