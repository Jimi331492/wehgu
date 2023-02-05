package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class OSSAuthPageQuery {
    @ApiModelProperty("关联UID")
    private String relationUuid;

    @ApiModelProperty("图片类型")
    private String storeTypeTable ;

    private boolean status;

}
