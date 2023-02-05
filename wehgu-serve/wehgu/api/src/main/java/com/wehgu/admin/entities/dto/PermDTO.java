package com.wehgu.admin.entities.dto;

import com.wehgu.admin.entities.vo.PermVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermDTO extends PermVO {
    /**
     * 上级权限ID
     */
    @ApiModelProperty("上级权限Id")
    private Integer parentId;
}
