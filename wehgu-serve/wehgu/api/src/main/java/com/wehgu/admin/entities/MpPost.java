package com.wehgu.admin.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Data
public class MpPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer postId;

    /**
     * 	系统保留字段，UUID
     */
    private String postUuid;

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
     * 外键 链接用户 多对一
     */
    private String userDetailUuid;

    /**
     * 发布内容
     */
    private String content;

    /**
     * 图片数量
     */
    private Integer pictureNum;

    /**
     * 点赞数量
     */
    private Integer starNum;

    /**
     * 评论数量
     */
    private Integer commentNum;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;

    /**
     * 标签外键
     */
    @ApiModelProperty("标签外键")
    private String tagUuid;


}
