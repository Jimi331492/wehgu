package com.wehgu.admin.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-04-07
 */
@ApiModel(description = "<p>  </p>")
@Data
@EqualsAndHashCode(callSuper = false)
public class MpTag implements Serializable {

    @ApiModelProperty("")
    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @ApiModelProperty("系统保留字段，自增ID")
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String tagUuid;

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
     * 标签名
     */
    @ApiModelProperty("标签名")
    private String title;

    /**
     * 标签字体颜色
     */
    @ApiModelProperty("标签字体颜色")
    private String color;

    /**
     * 标签背景颜色
     */
    @ApiModelProperty("标签背景颜色")
    private String backgroundColor;

    /**
     * 说明 冗余字段
     */
    @ApiModelProperty("说明 冗余字段")
    private String remark;

    /**
     * 该标签下帖子数量 冗余字段
     */
    @ApiModelProperty("该标签下帖子数量 冗余字段")
    private Integer postNum;

}
