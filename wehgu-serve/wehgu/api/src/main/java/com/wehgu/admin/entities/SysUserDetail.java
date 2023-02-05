package com.wehgu.admin.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(value = "user_detail_id", type = IdType.AUTO)
    private Integer userDetailId;

    /**
     * 	系统保留字段，UUID
     */
    private String userDetailUuid;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatar;

    /**
     * 个性签名
     */
    private String introduce;


    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * QQ
     */
    private String qq;

    /**
     * 用户类型 
     */
    private String type;

    /**
     * 身份验证，0-未通过，1-通过
     */
    private Boolean authentication;

    /**
     * 微信性别-0男 1女
     */
    private Integer gender;

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

    /**
     * 外键 用户和用户详情 一对一
     */
    private String userUuid;

    /**
     * 外键 角色和用户详情 多对一
     */
    private String roleUuid;

    /**
     * 外键 部门和用户详情 多对一
     */
    private String deptUuid;


    /**
     * 外键 角色和用户详情 多对一
     */
    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private String telephone;

    /**
     * 外键 部门和用户详情 多对一
     */
    @TableField(exist = false)
    private String deptName;


}
