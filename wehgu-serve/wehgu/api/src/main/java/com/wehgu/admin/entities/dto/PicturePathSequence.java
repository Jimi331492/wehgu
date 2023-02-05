package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PicturePathSequence {
    @ApiModelProperty(value = "图片路径")
    private String path;
    @ApiModelProperty(value = "加载顺序")
    private Integer sequence;
}
