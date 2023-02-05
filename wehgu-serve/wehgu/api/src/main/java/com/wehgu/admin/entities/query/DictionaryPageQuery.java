package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryPageQuery extends BasePageQuery{
    @ApiModelProperty(value = "键名")
    private String key;
}
