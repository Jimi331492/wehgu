package com.wehgu.admin.entities.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class CustomerVO extends BaseVO implements Serializable {

   @ApiModelProperty("账号UID")
   private String userDetailUuid;

   @ApiModelProperty("微信昵称")
   private String avatar;

   @ApiModelProperty("微信昵称")
   private String nickname;

   @ApiModelProperty("微信昵称")
   private Integer gender;

   @ApiModelProperty("身份验证，0-未通过，1-通过")
   private Boolean authentication;

   @ApiModelProperty("手机号")
   private String telephone;

   @ApiModelProperty("微信UnionId")
   private String unionId;

   @ApiModelProperty("userUuid")
   private String userUuid;

   @ApiModelProperty("状态")
   @JsonFormat(shape = JsonFormat.Shape.STRING)
   private Integer status;

   @ApiModelProperty("角色名")
   private String roleName;

   @ApiModelProperty("部门名")
   private String deptName;

}
