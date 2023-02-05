package com.wehgu.admin.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@Data
public class MpOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer orderId;

    /**
     * 	系统保留字段，UUID
     */
    private String orderUuid;

    /**
     * 	系统保留字段，添加时间
     */
    @ApiModelProperty("系统保留字段，添加时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    /**
     * 系统保留字段，最后更新时间
     */
    @ApiModelProperty("系统保留字段，最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 字典:订单状态
     */
    private String orderStatus;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 外键:用户收货地址
     */
    private String userLocationUuid;

    /**
     * 字典:支付方式
     */
    private String paymentMode;

    /**
     * 是否支付
     */
    private Boolean ifPay;

    /**
     * 包裹数量
     */
    private Integer packageNum;

    /**
     * 外键:代领人UID
     */
    private String agentUuid;
    /**
     * 期望：0男生 1女生 NULL无所谓
     */
    private String expected;

    /**
     * 最晚送达时间
     */
    private LocalDateTime deadlineTime;

}
