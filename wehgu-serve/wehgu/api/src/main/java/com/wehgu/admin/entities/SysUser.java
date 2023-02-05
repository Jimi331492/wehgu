package com.wehgu.admin.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
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
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 系统保留字段，UUID
     */
    private String userUuid;

    /**
     * 用户真实姓名
     */
    private String name;

    /**
     * 性别，如：男/女/未知
     */
    private String sex;

    /**
     * 民族
     */
    private String nation;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 生日
     */
    private LocalDateTime birth;

    /**
     * 入学年份 例：2018级
     */
    private String grade;

    /**
     * 预计毕业年份 2018级就是2022届
     */
    private String session;

    /**
     * 专业
     */
    private String major;

    /**
     * 学院
     */
    private String college;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 院校
     */
    @ApiModelProperty("院校")
    private String university;

    /**
     * 校区
     */
    @ApiModelProperty("校区")
    private String campus;

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


}
