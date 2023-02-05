package com.wehgu.admin.entities.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PicturePathSaveDTO {

    @ApiModelProperty(value = "图片对应实体:字典")
    private String entityName;
    @ApiModelProperty(value = "图片对应实体UID")
    private String entityUID;

    @ApiModelProperty(value = "路径")
    private List<PicturePathSequence> pathSequenceList;


}
