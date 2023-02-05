package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class NoticePageQuery extends BasePageQuery{
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 是否撤销
     */
    @ApiModelProperty("是否撤销")
    private Boolean ifRevoke;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Boolean ifDelete;


    /**
     * 通告类型 0全体用户 1单个用户 2用户类型 3多个用户
     */
    @ApiModelProperty("通告类型 0全体用户 1单个用户 2用户类型 3多个用户")
    private Integer type;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String userType;
}
