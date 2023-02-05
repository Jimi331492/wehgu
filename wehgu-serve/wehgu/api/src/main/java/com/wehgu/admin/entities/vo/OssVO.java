package com.wehgu.admin.entities.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OssVO extends BaseVO{

    @ApiModelProperty("主键ID")
    private Integer ossId;

    @ApiModelProperty("UUID")
    private String ossUuid;

    @ApiModelProperty("图片类型")
    private String storeTypeTable;

    @ApiModelProperty("关联UUID")
    private String relationUuid;

    @ApiModelProperty("相对路径")
    private String path;

    @ApiModelProperty("顺序")
    private Integer sequence;

    @ApiModelProperty("状态")
    private boolean status;

    @ApiModelProperty("UID")
    private String URI;

}
