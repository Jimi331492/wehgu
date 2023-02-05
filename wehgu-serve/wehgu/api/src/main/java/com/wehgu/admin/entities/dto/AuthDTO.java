package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuthDTO {
    /**
     * 用户真实姓名
     */
    @ApiModelProperty("用户真实姓名")
    private String name;

    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String studentNo;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    private String major;

    /**
     * 学院
     */
    @ApiModelProperty("学院")
    private String college;

    /**
     * 入学年份 例：2018级
     */
    @ApiModelProperty("入学年份 例：2018级")
    private String grade;

    /**
     * 性别，如：男/女/未知
     */
    @ApiModelProperty("性别，如：男/女/未知")
    private String sex;


    /**
     * 预计毕业年份 2018级就是2022届
     */
    @ApiModelProperty("预计毕业年份 2018级就是2022届")
    private String session;

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
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
}
