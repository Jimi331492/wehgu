package com.wehgu.admin.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@ApiModel(description = "<p>评论</p>")
@Data
@EqualsAndHashCode(callSuper = false)
public class MpComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @ApiModelProperty("系统保留字段，自增ID")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String commentUuid;

    /**
     * 父级评论 父级评论为0 表示他是回复帖子的
     */
    @ApiModelProperty("父级评论 父级评论为0 表示他是回复帖子的")
    private Integer parentId;

    /**
     * 	系统保留字段，添加时间
     */
    @ApiModelProperty("系统保留字段，添加时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    /**
     * 系统保留字段，最后更新时间
     */
    @ApiModelProperty("系统保留字段，最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 外键 链接帖子 为空代表为子评论
     */
    @ApiModelProperty("外键 链接帖子 为空代表为子评论")
    private String postUuid;

    /**
     * 外键：评论人
     */
    @ApiModelProperty("外键：评论人或回复人 主动方")
    private String fromUserDetailUuid;

    /**
     * 外键：评论人
     */
    @ApiModelProperty("外键：被回复人 为空代表回复帖子")
    private String toUserDetailUuid;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 点赞数量
     */
    @ApiModelProperty("点赞数量")
    private Integer starNum;

    /**
     * 评论的图片路径
     */
    @ApiModelProperty("评论的图片路径")
    private String path;

    @ApiModelProperty("子评论")
    @TableField(exist = false)
    private List<MpComment> children;


}
