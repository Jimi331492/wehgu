package com.wehgu.admin.config.security.login.customer;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.common.vo.UserInfoVO;
import com.wehgu.admin.config.security.chooser.LoginSolver;
import com.wehgu.admin.config.security.chooser.LoginSolverChooser;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.utils.CookieUtil;
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

/**
 * <p> 认证成功处理 </p>
 */
@Slf4j
@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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
        log.info("微信用户登录成功");
        String loginType = "MP";
        SecurityUser securityUser = ((SecurityUser) authentication.getPrincipal());
        JSONObject json = new JSONObject();
        ResultTemplate ret = new ResultTemplate(200, "登录成功!");

        //微信端的请求直接选择微信端loginSolver
        LoginSolver loginSolver = chooser.choose(Constant.WE_CHAT_MINI_PROGRAM);
        securityUser = loginSolver.solve(request, securityUser);

        UserInfoVO userInfoVO = new UserInfoVO();
        //复制到userInfoVO
        BeanUtil.copyProperties(securityUser.getCurrentUserInfo(), userInfoVO);
        json.put("userInfo", userInfoVO);

        // 生成jwt访问令牌
        String token = userDetailService.createToken(securityUser);
        json.put("X-Token", token);

        log.info("登录成功的token值 {}" , token);
        log.info("登录端 {}" , loginType);

        //设置cookie
        Cookie cookie = CookieUtil.generateLoginCookie(request, token);
        response.addCookie(cookie);

        ret.setData(json);
        ResponseUtil.out(response, ret);
    }


}
