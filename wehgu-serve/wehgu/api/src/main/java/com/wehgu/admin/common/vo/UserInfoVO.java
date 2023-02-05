package com.wehgu.admin.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.vo.RoleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserInfoVO implements Serializable {

    /**
     * 系统保留字段，UUID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userUuid;

    /**
     * 系统保留字段，UUID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userDetailUuid;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 邮箱
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    /**
     * QQ号码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qq;
    /**
     * 用户真实姓名
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    /**
     * 性别，如：男/女/未知
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;

    /**
     * 状态，如：1正常 0锁定
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer status;

    /**
     * 民族
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nation;

    /**
     * 身份证号 只返回第一位和最后一位
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String idCard;

    /**
     * 生日
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime birth;

    /**
     * 入学年份 例：2018级
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

    /**
     * 预计毕业年份 2018级就是2022届
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String session;

    /**
     * 专业
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String major;

    /**
     * 学院
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String college;

    /**
     * 学号
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String studentNo;


    /**
     * 校区
     */
    @ApiModelProperty("校区")
    private String campus;

    /**
     * 院校
     */
    @ApiModelProperty("院校")
    private String university;


    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatar;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 介绍
     */
    private Boolean authentication;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 角色
     */
    private RoleVO role;


    /**
     *  部门名
     */
    private String deptName;


    /**
     *  权限树列表
     */
    private List<SysPermission> permList;


}
