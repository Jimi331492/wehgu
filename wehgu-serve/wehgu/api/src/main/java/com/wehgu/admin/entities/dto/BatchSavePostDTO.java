package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatchSavePostDTO {
    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;

    /**
     * 帖子UID数组
     */
    @ApiModelProperty("帖子UID数组")
    private List<String> postUIDList;
}
