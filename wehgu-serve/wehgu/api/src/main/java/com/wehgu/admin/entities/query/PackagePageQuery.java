package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PackagePageQuery extends BasePageQuery{
    @ApiModelProperty(value = "账号")
    private String userDetailUuid;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "驿站")
    private String pickUpSite;

    @ApiModelProperty(value = "取件码")
    private String pickUpCode;

    @ApiModelProperty(value = "订单状态")
    private String orderStatus;
}
