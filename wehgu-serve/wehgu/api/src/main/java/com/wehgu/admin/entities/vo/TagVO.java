package com.wehgu.admin.entities.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;



@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class TagVO extends BaseVO{
    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String tagUuid;


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
