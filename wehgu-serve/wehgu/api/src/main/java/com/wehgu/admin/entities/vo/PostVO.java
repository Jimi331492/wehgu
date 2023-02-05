package com.wehgu.admin.entities.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PostVO extends BaseVO{


    /**
     * PostUID
     */
    @ApiModelProperty("PostUID")
    private String postUuid;

    /**
     * 发布用户昵称
     */
    @ApiModelProperty("发布用户昵称")
    private String nickname;

    /**
     * 发布用户UID
     */
    @ApiModelProperty("发布用户UID")
    private String userDetailUuid;


    /**
     * 发布用户手机号
     */
    @ApiModelProperty("发布用户手机号")
    private String telephone;

    /**
     * 发布用户头像
     */
    @ApiModelProperty("发布用户头像")
    private String avatar;

    /**
     * 图片数量
     */
    @ApiModelProperty("图片数量")
    private String pictureNum;


    /**
     * 标签
     */
    @ApiModelProperty("标签")
    @JsonProperty(value = "tag")
    private String title;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;
    /**
     * 标签
     */
    @ApiModelProperty("标签背景颜色")
    private String backgroundColor;

    /**
     * 标签
     */
    @ApiModelProperty("标签字体颜色")
    private String color;


    /**
     * 发布内容
     */
    @ApiModelProperty("发布内容")
    private String content;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer star;

    /**
     * 点赞数量
     */
    @ApiModelProperty("点赞数")
    private Integer commentNum;


    /**
     * 发布图片相对路径列表
     */
    @ApiModelProperty("发布图片相对路径列表")
    private List<String> ImgPathList;

    /**
     * 第一张图片URI
     */
    @ApiModelProperty("第一张图片URI")
    private String ImgIndexURI;


    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private List<CommentVO> commentList;

}
