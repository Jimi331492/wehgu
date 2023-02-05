package com.wehgu.admin.config.security.login.customer;

import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 认证失败处理 - 前后端分离情况下返回json数据格式 </p>
 *
 */
@Slf4j
@Component
public class CustomerAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) {
        ResultTemplate result;
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            result = ResultTemplate.err(e.getMessage());
        } else if (e instanceof LockedException) {
            result = ResultTemplate.err("账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            result = ResultTemplate.err("证书过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            result = ResultTemplate.err("账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            result = ResultTemplate.err("账户被禁用，请联系管理员!");
        } else {
            log.error("登录失败：", e);
            result = ResultTemplate.err("登录失败!");
        }
        ResponseUtil.out(response, result);
    }

}