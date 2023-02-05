package com.wehgu.admin.entities.vo;

import com.wehgu.admin.entities.SysUser;
import com.wehgu.admin.entities.dto.AuthDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OssAuthVO extends BaseVO{

    /**
     * 院校
     */
    @ApiModelProperty("院校")
    private String university;

    @ApiModelProperty("主键ID")
    private Integer ossId;

    @ApiModelProperty("UUID")
    private String ossUuid;

    @ApiModelProperty("图片类型")
    private String storeTypeTable;

    @ApiModelProperty("关联UUID")
    private String relationUuid;

    @ApiModelProperty("相对路径")
    private String path;

    @ApiModelProperty("顺序")
    private Integer sequence;

    @ApiModelProperty("状态")
    private boolean status;

    @ApiModelProperty("UID")
    private String URI;


    @ApiModelProperty("账号昵称")
    private String nickname;

    @ApiModelProperty("账号头像")
    private String avatar;

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
     * 校区
     */
    @ApiModelProperty("校区")
    private String campus;

    @ApiModelProperty("账号UID")
    private String userDetailUuid;



}
