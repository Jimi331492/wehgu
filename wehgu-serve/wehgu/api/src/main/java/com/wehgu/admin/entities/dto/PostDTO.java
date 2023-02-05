package com.wehgu.admin.entities.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel
@Data
public class PostDTO {


    /**
     * PostUID
     */
    @ApiModelProperty("PostUID")
    private String postUuid;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String tag;

    /**
     * 发布内容
     */
    @ApiModelProperty("发布内容")
    private String content;

    /**
     * 图片数量
     */
    @ApiModelProperty("图片数量")
    private Integer pictureNum;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;

    /**
     * 外键 链接用户 多对一
     */
    @ApiModelProperty("外键 链接用户 多对一")
    private String userDetailUuid;


}
