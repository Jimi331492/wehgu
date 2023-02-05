package com.wehgu.admin.entities;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
@ApiModel(description = "<p>  </p>")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysPermission  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @ApiModelProperty("系统保留字段，自增ID")
    @TableId(value = "perm_id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer permId;

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String permUuid;

    /**
     * 上级权限ID
     */
    @ApiModelProperty("上级权限ID")
    private Integer parentId;

    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String title;

    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    private String auth;

    /**
     * 权限类型 0 菜单/ 1按钮
     */
    @ApiModelProperty("权限类型 0 菜单/ 1按钮")
    private Integer type;

    /**
     * 菜单的URL
     */
    @ApiModelProperty("菜单的URL")
    private String url;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;

    /**
     * 菜单排序
     */
    @ApiModelProperty("菜单排序")
    private Integer orderNum;

    /**
     * 	系统保留字段，添加时间
     */
    @ApiModelProperty("系统保留字段，添加时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    /**
     * 系统保留字段，最后更新时间
     */
    @ApiModelProperty("系统保留字段，最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    @TableField(exist = false)
    private String parentTitle;

    @ApiModelProperty("")
    @TableField(exist = false)
    private List<SysPermission> children;
}
