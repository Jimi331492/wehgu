package com.wehgu.admin.config.security.login;


import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 认证权限入口 - 未登录的情况下访问所有接口都会拦截到此 </p>
 *
 */
@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        // 未登录 或 token过期
        if (e != null) {
            ResponseUtil.out(response, ResultTemplate.expired(e.getMessage()));
        } else {
            ResponseUtil.out(response, ResultTemplate.expired("Token过期!"));
        }
    }

}
