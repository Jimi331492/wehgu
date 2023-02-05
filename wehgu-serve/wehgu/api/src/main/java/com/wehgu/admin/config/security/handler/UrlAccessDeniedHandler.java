package com.wehgu.admin.config.security.handler;


import com.wehgu.admin.common.ResultCode;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p> 认证url权限 - 登录后访问接口无权限 - 自定义403无权限响应内容 </p>
 *
 */
@Component
public class UrlAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        ResponseUtil.out(response, ResultTemplate.err(ResultCode.UNAUTHORIZED.getCode(), e.getMessage()));
    }
}
