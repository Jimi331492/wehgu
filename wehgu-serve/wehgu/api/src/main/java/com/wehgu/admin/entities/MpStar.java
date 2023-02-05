package com.wehgu.admin.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
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
@ApiModel(description = "<p> 点赞 </p>")
@Data
@EqualsAndHashCode(callSuper = false)
public class MpStar implements Serializable{


    private static final long serialVersionUID = 1L;


    /**
     * 被点赞的  帖子 或评论的uuid
     */
    @ApiModelProperty("被点赞的  帖子 或评论的uuid")
    @MppMultiId
    private String linkedUuid;

    /**
     * 点赞人
     */
    @ApiModelProperty("点赞人")
    @MppMultiId
    private String userDetailUuid;

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


}
