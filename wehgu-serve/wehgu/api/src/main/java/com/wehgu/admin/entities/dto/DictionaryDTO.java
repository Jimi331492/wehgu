package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DictionaryDTO {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Integer id;

    /**
     * 键名
     */
    @ApiModelProperty("键名")
    private String key;


    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String remark;

    /**
     * 值
     */
    @ApiModelProperty("值：列表")
    private List<String> valueList;


}
