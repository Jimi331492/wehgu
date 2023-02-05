package com.wehgu.admin.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.wehgu.admin.config.Constants;
import com.wehgu.admin.config.security.dto.GlobalUser;
import com.wehgu.admin.utils.MultiReadHttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 自定义用户密码校验过滤器 </p>
 */

public class UserAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    //todo：登陆最先访问1，然后验证用户名和密码，
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().contains(Constants.REQUEST_HEADERS_CONTENT_TYPE)) {
            throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
        }

        UsernamePasswordAuthenticationToken authRequest;
        try {
            MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);
            // 将前端传递的数据转换成jsonBean数据格式
            GlobalUser globalUser = JSONObject.parseObject(wrappedRequest.getBodyJsonStrByJson(wrappedRequest), GlobalUser.class);
            //这里username实际传递参数为手机号码
            authRequest = new UsernamePasswordAuthenticationToken(globalUser.getTelephone(), globalUser.getPassword(), null);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
