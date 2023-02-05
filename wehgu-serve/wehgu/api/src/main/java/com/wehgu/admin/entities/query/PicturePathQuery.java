package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "系统管理-操作员查询参数")
public class PicturePathQuery {
    @ApiModelProperty(value = "图片对应实体:字典")
    private String entityName;
    @ApiModelProperty(value = "图片对应实体UID")
    private String entityUID;
}