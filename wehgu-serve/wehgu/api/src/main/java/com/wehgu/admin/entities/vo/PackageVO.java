package com.wehgu.admin.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PackageVO {
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

    /**
     * 所属订单UID
     */
    @ApiModelProperty("所属订单UID")
    private String orderUuid;
    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

}
