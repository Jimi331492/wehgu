package com.wehgu.admin.entities.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVO extends BaseVO{
    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String orderUuid;


    /**
     * 字典:订单状态
     */
    @ApiModelProperty("字典:订单状态")
    private String orderStatus;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 外键:用户收货地址
     */
    @ApiModelProperty("外键:用户收货地址")
    private String userLocationUuid;

    /**
     * 外键:用户UID
     */
    @ApiModelProperty("外键:用户UID")
    private String userDetailUuid;

    /**
     * 将包裹送达的位置
     */
    @ApiModelProperty("将包裹送达的位置")
    private String deliveryLocation;

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
    /**
     * 字典:支付方式
     */
    @ApiModelProperty("字典:支付方式")
    private String paymentMode;

    /**
     * 是否支付
     */
    @ApiModelProperty("是否支付")
    private Boolean ifPay;

    /**
     * 包裹数量
     */
    @ApiModelProperty("包裹数量")
    private Integer packageNum;

    /**
     * 期望：0男生 1女生 NULL无所谓
     */
    @ApiModelProperty("期望：0男生 1女生 NULL无所谓")
    private String expected;

    /**
     * 最晚送达时间
     */
    @ApiModelProperty("最晚送达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime deadlineTime;

    /**
     * 包裹数量
     */
    @ApiModelProperty("包裹数量")
    private List<PackageVO> packageList;

    @ApiModelProperty("发布人信息")
    private UserInfoVO userInfoVO;
}
