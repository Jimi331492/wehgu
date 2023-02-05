package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageQuery extends BasePageQuery {
    @ApiModelProperty(value = "真实姓名")
    private String name;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "电话号")
    private String telephone;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "账号状态")
    private Integer status;

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

    private String roleUID;

    private String deptUID;

}
