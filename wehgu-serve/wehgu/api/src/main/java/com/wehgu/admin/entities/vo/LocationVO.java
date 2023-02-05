package com.wehgu.admin.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;



@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class LocationVO extends BaseVO{

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String userLocationUuid;

    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;


    /**
     * 外键:用户UID
     */
    @ApiModelProperty("外键:用户UID")
    private String userDetailUuid;

    /**
     * 外键:用户UID
     */
    @ApiModelProperty("外键:用户UID")
    @TableField(exist = false)
    private String nickname;

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
}
