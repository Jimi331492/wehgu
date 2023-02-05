package com.wehgu.admin.entities.vo;

import com.wehgu.admin.entities.dto.AuthDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;

@ApiModel
@Data
public class AuthVO {

    @ApiModelProperty("账号UID")
    private String userDetailUuid;

    @ApiModelProperty("账号昵称")
    private String nickname;

    @ApiModelProperty("账号头像")
    private String avatar;

    @ApiModelProperty("学生证URI列表")
    private List<String> studentCardURI;

    @ApiModelProperty("认证信息")
    private AuthDTO authDTO;
}
