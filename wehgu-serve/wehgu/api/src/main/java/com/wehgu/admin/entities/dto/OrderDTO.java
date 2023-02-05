package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
public class OrderDTO {
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
    private LocalDateTime deadlineTime;

    /**
     * 包裹数量
     */
    @ApiModelProperty("包裹UUID数组")
    private List<String> packageUIDList;
}
