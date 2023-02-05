package com.wehgu.admin.entities.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel
@Data
public class CustomerDTO {
    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String userDetailUuid;

    /**
     * 微信昵称
     */
    @ApiModelProperty("微信昵称")
    private String nickname;

    /**
     * 微信头像
     */
    @ApiModelProperty("微信头像")
    private String avatar;

    /**
     * 个性签名
     */
    @ApiModelProperty("个性签名")
    private String introduce;

    /**
     * 身份验证，0-未通过，1-通过
     */
    @ApiModelProperty("身份验证")
    private Boolean authentication;


    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private Integer status;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信openId")
    private String openId;

    /**
     * 微信unionId
     */
    @ApiModelProperty("微信unionId")
    private String unionId;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 盐
     */
    @ApiModelProperty("盐")
    private String salt;

    /**
     * QQ
     */
    @ApiModelProperty("QQ")
    private String qq;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String type;

    /**
     * 外键 用户和用户详情 一对一
     */
    @ApiModelProperty("外键 用户和用户详情 一对一")
    private String userUuid;

    /**
     * 外键 角色和用户详情 多对一
     */
    @ApiModelProperty("外键 角色和用户详情 多对一")
    private String roleUuid;

    /**
     * 外键 部门和用户详情 多对一
     */
    @ApiModelProperty("外键 部门和用户详情 多对一")
    private String deptUuid;



}
