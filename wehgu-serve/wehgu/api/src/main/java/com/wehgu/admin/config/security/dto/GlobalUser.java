package com.wehgu.admin.config.security.dto;



import com.wehgu.admin.common.vo.UserInfoVO;
import com.wehgu.admin.entities.SysPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalUser extends UserInfoVO {


    /**
     * 用户登录密码
     */
    private String password;


    /**
     * 微信unionId
     */
    private String unionId;


}
