package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class LikedCountDTO {

    @ApiModelProperty("0帖子 1评论")
    private Integer type;

    @ApiModelProperty("被点赞的帖子或评论Uuid")
    private String linkedUuid;

    @ApiModelProperty("点赞数量")
    private Integer starNum;

}
