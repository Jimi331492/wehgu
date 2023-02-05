package com.wehgu.admin.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统保留字段，自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 键
     */
    @TableField("`key`")
    private String key;

    /**
     * 值
     */
    @TableField("`value`")
    private String value;

    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String remark;

    /**
     * 	系统保留字段，添加时间
     */
    @ApiModelProperty("系统保留字段，添加时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime addTime;

    /**
     * 系统保留字段，最后更新时间
     */
    @ApiModelProperty("系统保留字段，最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;


}
