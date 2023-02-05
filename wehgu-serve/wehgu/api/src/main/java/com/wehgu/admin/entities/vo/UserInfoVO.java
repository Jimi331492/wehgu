package com.wehgu.admin.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoVO {

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("微信昵称")
    private String avatar;

    @ApiModelProperty("微信昵称")
    private String nickname;

    @ApiModelProperty("微信昵称")
    private Integer gender;

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
     * 校区
     */
    @ApiModelProperty("校区")
    private String campus;

    /**
     * 院校
     */
    @ApiModelProperty("院校")
    private String university;

    @ApiModelProperty("身份验证，0-未通过，1-通过")
    private Boolean authentication;

}
