package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class MpLoginDTO {

    @ApiModelProperty("微信签名")
    private String signature;
    @ApiModelProperty("用户信息")
    private String rawData;
    @ApiModelProperty("加密数据")
    private String encryptedData;
    @ApiModelProperty("iv")
    private String iv;
    @ApiModelProperty("调用wx.login生成的jscode")
    private String jscode;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("手机号")
    private String telephone;
}
