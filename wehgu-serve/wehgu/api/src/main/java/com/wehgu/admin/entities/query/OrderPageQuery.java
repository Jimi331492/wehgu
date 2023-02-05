package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageQuery extends BasePageQuery{
    @ApiModelProperty(value = "账号")
    private String userDetailUuid;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "账号绑定手机号")
    private String telephone;

    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneeTelephone;

    @ApiModelProperty(value = "取件码")
    private String pickUpCode;

    @ApiModelProperty(value = "驿站")
    private String pickUpSite;

    /**
     * 时间起
     */
    @ApiModelProperty("时间起")
    private LocalDateTime startTime;

    /**
     * 时间止
     */
    @ApiModelProperty("时间止")
    private LocalDateTime endTime;

}
