package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class StarDTO {


    /**
     * 点赞的人UID
     */
    @ApiModelProperty("点赞的人UID")
    private String userDetailUuid;


    /**
     * 被点赞的帖子的uuid
     */
    @ApiModelProperty("被点赞的帖子或评论的uuid")
    private String linkedUuid;


    /**
     * 类型 0 帖子 1评论
     */
    @ApiModelProperty("类型 0 帖子 1评论")
    private Integer type;


    /**
     * 状态 0取消点赞 1点赞
     */
    @ApiModelProperty("状态 0取消点赞 1点赞")
    private Integer status;


}
