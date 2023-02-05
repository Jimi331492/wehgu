package com.wehgu.admin.config.security.login.user;

import com.alibaba.fastjson.JSONObject;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.config.security.chooser.LoginSolver;
import com.wehgu.admin.config.security.chooser.LoginSolverChooser;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.CookieUtil;
import com.wehgu.admin.utils.JwtUtil;
import com.wehgu.admin.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p> 认证成功处理 </p>
 */
@Slf4j
@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private LoginSolverChooser chooser;

    @Resource
    private UserDetailsServiceImpl userDetailService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

    }

    //todo 登陆生成token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("用户登录成功");
        String loginType = "PC";
        SecurityUser securityUser = ((SecurityUser) authentication.getPrincipal());
        JSONObject json = new JSONObject();
        ResultTemplate ret = new ResultTemplate(200,"登录成功!");

        String userAgent = request.getHeader("user-agent").toLowerCase();
        //先判断是不是微信端的请求
        if (userAgent.toLowerCase().contains("micromessenger")) {
            //微信端的请求直接保存unionId和sessionId
            String sessionId = request.getHeader(Constant.WX_SP_SESSIONID_HEADER);
            String unionid = request.getHeader(Constant.WX_SP_UNIONID_HEADER);
            if (StringUtils.isNotBlank(sessionId) || StringUtils.isNotBlank(unionid)){
                LoginSolver loginSolver = chooser.choose(Constant.WE_CHAT_MINI_PROGRAM);
                securityUser = loginSolver.solve(request, securityUser);
            }else {
                LoginSolver loginSolver = chooser.choose(Constant.WE_CHAT_PUBLIC_ACCOUNT);
                securityUser = loginSolver.solve(request, securityUser);
            }
            loginType = "SP";
        }else {
            LoginSolver loginSolver = chooser.choose(Constant.PC);
            securityUser = loginSolver.solve(request, securityUser);
        }


        // 生成jwt访问令牌
        String token = userDetailService.createToken(securityUser);

        System.out.println("登录成功的token值=" + token);
        System.out.println("登录端" + loginType);

        //设置cookie
        Cookie cookie = CookieUtil.generateLoginCookie(request, token);
        response.addCookie(cookie);

        //返回token和userId
        json.put("X-Token", token);
        json.put("userUID",securityUser.getCurrentUserInfo().getUserUuid());
        ret.setData(json);
        ResponseUtil.out(response, ret);
    }


}
