package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class LocationDTO {

    /**
     * 将包裹送达的位置
     */
    @ApiModelProperty("地址UID")
    private String userLocationUuid;
    /**
     * 将包裹送达的位置
     */
    @ApiModelProperty("将包裹送达的位置")
    private String deliveryLocation;

    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;


    /**
     * 收货人电话
     */
    @ApiModelProperty("收货人电话")
    private String consigneeTelephone;

    /**
     * 收货人姓名
     */
    @ApiModelProperty("收货人姓名")
    private String consignee;
}
