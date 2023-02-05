package com.wehgu.admin.entities.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel
@Data
public class CommentDTO {


    /**
     * CommentId
     */
    @ApiModelProperty("CommentId")
    @TableId(type = IdType.AUTO)
    private Integer CommentId;

    /**
     * CommentUID
     */
    @ApiModelProperty("CommentUID")
    private String CommentUuid;

    /**
     * 评论用户：谁评论的
     */
    @ApiModelProperty("评论或回复用户：谁评论的 主动方")
    private String fromUserDetailUuid;


    /**
     * 评论用户：谁评论的
     */
    @ApiModelProperty("被回复用户：被动方 回复谁的")
    private String toUserDetailUuid;

    /**
     * 被评论的：谁评论的
     */
    @ApiModelProperty("被评论的帖子：为NULL的话 parentId不能为0")
    private String postUuid;

    @ApiModelProperty("父级评论 为0 postUuid不能为NULL")
    private Integer parentId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 评论图片路径
     */
    @ApiModelProperty("图片路径")
    private String path;


}
