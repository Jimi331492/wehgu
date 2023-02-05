package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagPageQuery extends BasePageQuery{

    /**
     * 标签名
     */
    @ApiModelProperty("标签名")
    private String title;
}
