package com.wehgu.admin.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class CarouseVO extends BaseVO{
    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String carouselUuid;

    /**
     * 轮播图title
     */
    @ApiModelProperty("轮播图title")
    private String title;

    /**
     * 权重 越大顺序越靠前
     */
    @ApiModelProperty("权重 越大顺序越靠前")
    private Integer weight;


    /**
     * 当前展示状态 0不展示 1展示
     */
    @ApiModelProperty("当前展示状态 0不展示 1展示")
    private Boolean status;

    /**
     * 计划开始展示时间
     */
    @ApiModelProperty("计划开始展示时间")
    private LocalDateTime startTime;

    /**
     * 计划结束展示时间
     */
    @ApiModelProperty("计划结束展示时间")
    private LocalDateTime endTime;

    /**
     * 轮播图点链接路径
     */
    @ApiModelProperty("轮播图点链接路径")
    private String link;

    /**
     * 运营位 将来可能不止有一个轮播图
     */
    @ApiModelProperty("运营位 将来可能不止有一个轮播图")
    private String location;


    /**
     * 	图片路径
     */
    @ApiModelProperty("图片相对路径")
    @TableField(exist = false)
    private String path;


    /**
     * 	图片网络路径
     */
    @ApiModelProperty("图片网络路径")
    @TableField(exist = false)
    private String URI;

}
