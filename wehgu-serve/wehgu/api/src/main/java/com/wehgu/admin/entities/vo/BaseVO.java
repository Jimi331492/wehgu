package com.wehgu.admin.entities.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseVO {
    /**
     * 	系统保留字段，添加时间
     */
    @ApiModelProperty("系统保留字段，添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime addTime;

    /**
     * 系统保留字段，最后更新时间
     */
    @ApiModelProperty("系统保留字段，最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
}
