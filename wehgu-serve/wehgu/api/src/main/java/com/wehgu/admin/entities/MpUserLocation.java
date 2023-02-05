package com.wehgu.admin.entities;

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
public class MpUserLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer userLocationId;

    /**
     * 	系统保留字段，UUID
     */
    private String userLocationUuid;

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
     * 外键:用户UID
     */
    private String userDetailUuid;

    /**
     * 将包裹送达的位置
     */
    private String deliveryLocation;

    /**
     * 收货人电话
     */
    private String consigneeTelephone;

    /**
     * 收货人姓名
     */
    private String consignee;

    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;


}
