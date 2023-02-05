package com.wehgu.admin.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 轮播图表
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MpCarousel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(value = "carousel_id", type = IdType.AUTO)
    private Integer carouselId;

    /**
     * 	系统保留字段，UUID
     */
    private String carouselUuid;

    /**
     * 轮播图title
     */
    private String title;

    /**
     * 权重 越大顺序越靠前
     */
    private Integer weight;


    /**
     * 当前展示状态 0不展示 1展示
     */
    private Boolean status;

    /**
     * 计划开始展示时间
     */
    private LocalDateTime startTime;

    /**
     * 计划结束展示时间
     */
    private LocalDateTime endTime;

    /**
     * 轮播图点击跳转路径
     */
    private String link;

    /**
     * 运营位 将来可能不止有一个轮播图
     */
    private String location;

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
