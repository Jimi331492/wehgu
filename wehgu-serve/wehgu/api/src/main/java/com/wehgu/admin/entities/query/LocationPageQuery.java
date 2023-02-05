package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationPageQuery extends BasePageQuery{
    @ApiModelProperty(value = "账号")
    private String userDetailUuid;

    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneeTelephone;

    @ApiModelProperty(value = "收货地址")
    private String deliveryLocation;
}
