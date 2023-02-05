package com.wehgu.admin.config.security.login.customer;

import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.PasswordUtil;
import com.wehgu.admin.utils.SmsUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p> 自定义认证处理 </p>
 */
@Component(value = "customerAuthenticationProvider")
public class CustomerAuthenticationProvider implements AuthenticationProvider {

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Override
    //todo 登陆2 // 验证密码
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取前端表单中输入后返回的unionId、appId
        String telephone = (String) authentication.getPrincipal();

        // 查找用户
        SecurityUser userInfo = (SecurityUser) userDetailsService.loadUserByUsername(telephone);


        return new UsernamePasswordAuthenticationToken(userInfo, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}