package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StarPageQuery extends BasePageQuery {


    /**
     * 被点赞的帖子的uuid
     */
    @ApiModelProperty("被点赞的帖子或评论的uuid")
    private String linkedUuid;

    /**
     * 状态 0取消点赞 1点赞
     */
    @ApiModelProperty("状态 0取消点赞 1点赞")
    private Integer status;

    /**
     * 类型 0 点赞帖子 1 点赞评论
     */
    @ApiModelProperty("类型 0 点赞帖子 1 点赞评论")
    private Integer type;

    /**
     * 点赞的人UID
     */
    @ApiModelProperty("点赞的人UID")
    private String userDetailUuid;

    /**
     * 点赞的人昵称
     */
    @ApiModelProperty("点赞的人昵称")
    private String nickname;

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
