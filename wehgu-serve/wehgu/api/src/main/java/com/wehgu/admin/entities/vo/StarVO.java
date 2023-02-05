package com.wehgu.admin.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;



@EqualsAndHashCode(callSuper = true)
@Data
public class StarVO extends BaseVO{



    /**
     * 被点赞的评论UID 或者 帖子UID
     */
    @ApiModelProperty("被点赞的评论UID 或者 帖子UID")
    private String linkedUuid;

    /**
     * 被点赞的评论UID 或者 帖子UID
     */
    @ApiModelProperty("被点赞的评论UID 或者 帖子UID")
    private String userDetailUuid;

    /**
     * 类型 0 点赞帖子 1 点赞评论
     */
    @ApiModelProperty("类型 0 点赞帖子 1 点赞评论")
    private Integer type;

    /**
     * 状态 0取消点赞 1点赞
     */
    @ApiModelProperty("状态 0取消点赞 1点赞")
    private Integer status;


    /**
     * 被点赞的内容
     */
    @ApiModelProperty("被点赞的内容")
    private String content;

    /**
     * 点赞用户昵称
     */
    @ApiModelProperty("点赞用户昵称")
    private String nickname;



}
