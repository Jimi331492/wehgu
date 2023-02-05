package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatchSaveOSSDTO {
    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private boolean status;

    /**
     * 帖子UID数组
     */
    @ApiModelProperty("图片UID数组")
    private List<String> UIDList;
}
