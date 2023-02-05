package com.wehgu.admin.entities.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PostPageQuery extends BasePageQuery{

    /**
     * 发布内容
     */
    @ApiModelProperty("发布内容")
    private String content;

    /**
     * 发布用户UID
     */
    @ApiModelProperty("发布用户UID")
    private String userDetailUuid;

    /**
     * 发布用户昵称
     */
    @ApiModelProperty("发布用户昵称")
    private String nickname;

    /**
     * 发布用户手机号
     */
    @ApiModelProperty("发布用户手机号")
    private String telephone;


    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;

    /**
     * 标签
     */
    @ApiModelProperty("标签名")
    private String tag;


    /**
     * 排序模式
     */
    @ApiModelProperty("排序字段")
    private String sort;

    /**
     * 排序模式
     */
    @ApiModelProperty("排序模式 desc递减，asc递增")
    private String order;

    /**
     * 时间起
     */
    @ApiModelProperty("时间起")
    private LocalDateTime startTime;

    /**
     * 时间止
     */
    @ApiModelProperty("时间止")
    private LocalDateTime endTime;
}
