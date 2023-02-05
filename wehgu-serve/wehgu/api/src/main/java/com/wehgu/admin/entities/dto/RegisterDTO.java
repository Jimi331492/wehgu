package com.wehgu.admin.entities.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class RegisterDTO {
    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 手机号码
     */
    private String password;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 角色UID
     */
    private String roleUID;

    /**
     * 部门UID
     */
    private String deptUID;
}
