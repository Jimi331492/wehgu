package com.wehgu.admin.config.security.login.user;

import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.PasswordUtil;
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
@Component(value = "userAuthenticationProvider")
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Resource
    UserDetailsServiceImpl userDetailsService;


    @Override
    //todo 登陆2 // 验证密码
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取前端表单中输入后返回的用户名、密码
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // 查找密码
        SecurityUser userInfo = (SecurityUser) userDetailsService.loadUserByUsername(username);

        // 验证密码
        boolean isValid = PasswordUtil.isValidPassword(password, userInfo.getPassword());

        if (!isValid) {
            throw new BadCredentialsException("密码错误！");
        }
        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}