package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class LikedPostPageQuery extends BasePageQuery{

    /**
     * 点赞的人UID
     */
    @ApiModelProperty("点赞的人UID")
    private String userDetailUuid;

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
