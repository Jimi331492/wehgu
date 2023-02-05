package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CarousePageQuery extends  BasePageQuery{

    /**
     * 当前展示状态 0不展示 1展示
     */
    @ApiModelProperty("当前展示状态 0不展示 1展示")
    private Boolean status;

    /**
     * 运营位 将来可能不止有一个轮播图
     */
    @ApiModelProperty("运营位 将来可能不止有一个轮播图")
    private String location;

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String carouselUuid;

    /**
     * 	当前时间
     */
    @ApiModelProperty("当前时间")
    private LocalDateTime nowTime;

    /**
     * 轮播图title
     */
    @ApiModelProperty("轮播图title")
    private String title;
}
