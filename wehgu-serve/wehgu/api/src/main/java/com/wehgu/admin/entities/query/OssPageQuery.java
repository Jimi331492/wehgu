package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class OssPageQuery extends BasePageQuery{

    @ApiModelProperty("图片类型")
    private String storeTypeTable;

    @ApiModelProperty("关联UID")
    private String relationUuid;

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
