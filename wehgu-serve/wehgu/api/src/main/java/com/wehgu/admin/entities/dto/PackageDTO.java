package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class PackageDTO {
    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String packageUuid;

    /**
     * 取件码
     */
    @ApiModelProperty("取件码")
    private String pickUpCode;

    /**
     * 驿站点
     */
    @ApiModelProperty("驿站点")
    private String pickUpSite;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;



}
