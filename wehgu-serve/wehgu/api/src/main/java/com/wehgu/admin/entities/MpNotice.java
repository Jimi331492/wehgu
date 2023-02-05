package com.wehgu.admin.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@ApiModel(description = "<p>  </p>")
@Data
public class MpNotice implements Serializable {

    @ApiModelProperty("")
    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @ApiModelProperty("系统保留字段，自增ID")
    @TableId(type = IdType.AUTO)
    private Integer noticeId;

    /**
     * 	系统保留字段，UUID
     */
    @ApiModelProperty("系统保留字段，UUID")
    private String noticeUuid;

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
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 是否撤销
     */
    @ApiModelProperty("是否撤销")
    private Boolean ifRevoke;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Boolean ifDelete;

    /**
     * 撤销时间
     */
    @ApiModelProperty("撤销时间")
    private LocalDateTime revokeTime;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;

    /**
     * 通告类型 0全体用户 1单个用户 2用户类型 3多个用户
     */
    @ApiModelProperty("通告类型 0全体用户 1单个用户 2用户类型 3多个用户")
    private Integer type;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String userType;


}
