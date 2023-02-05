package com.wehgu.admin.entities.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentVO extends BaseVO{

    /**
     * 评论Id
     */
    @ApiModelProperty("评论Id")
    private Integer commentId;

    /**
     * 评论UID
     */
    @ApiModelProperty("评论UID")
    private String commentUuid;

    /**
     * 评论或回复用户UID
     */
    @ApiModelProperty("评论或回复用户UID")
    private String fromUserDetailUuid;

    /**
     * 被回复用户UID
     */
    @ApiModelProperty("被回复用户UID")
    private String toUserDetailUuid;

    /**
     * 回复用户昵称
     */
    @ApiModelProperty("评论或回复用户昵称")
    private String fromNickname;

    /**
     * 回复用户昵称
     */
    @ApiModelProperty("被回复用户昵称")
    private String toNickname;

    /**
     * 发布用户头像
     */
    @ApiModelProperty("评论或回复用户头像")
    private String fromAvatar;

    /**
     * 发布用户头像
     */
    @ApiModelProperty("评论或回复用户头像")
    private String toAvatar;



    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer star;

    /**
     * 评论的图片路径
     */
    private String path;

    /**
     * 评论的图片网络路径
     */
    private String URI;

    /**
     * 父级评论 父级评论为0 表示他是回复帖子的
     */
    private Integer parentId;

    /**
     * 外键 被评论的帖子 为空代表为子评论
     */
    private String postUuid;

    /**
     * 子评论
     */
    private List<CommentVO> children;
}
