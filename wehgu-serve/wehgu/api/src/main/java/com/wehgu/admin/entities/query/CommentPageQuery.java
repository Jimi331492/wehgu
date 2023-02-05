package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentPageQuery extends BasePageQuery{

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 评论内容
     */
    @ApiModelProperty("主动评论人")
    private String nickname;

    /**
     * 评论用户UID
     */
    @ApiModelProperty("评论用户UID")
    private String fromUserDetailUuid;

    /**
     * 被评论的帖子UID
     */
    @ApiModelProperty("被评论的帖子UID")
    private String postUuid;

    /**
     * 被回复的评论的Id
     */
    @ApiModelProperty("被回复的评论的Id")
    private Integer parentId;


    /**
     * 被评论的评论
     */
    @ApiModelProperty("被评论的评论")
    private String commentUuid;

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
