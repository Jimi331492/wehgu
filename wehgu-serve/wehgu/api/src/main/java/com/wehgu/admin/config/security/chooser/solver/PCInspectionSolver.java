package com.wehgu.admin.config.security.chooser.solver;

import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.Constants;
import com.wehgu.admin.config.security.chooser.InspectionSolver;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.utils.CookieUtil;
import com.wehgu.admin.utils.MultiReadHttpServletRequest;
import com.wehgu.admin.utils.MultiReadHttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PCInspectionSolver extends InspectionSolver {


    @Override
    public UsernamePasswordAuthenticationToken solve(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response, UserDetailsServiceImpl detailsService) {
        log.info("PC操作");

        // 获取header中获取token信息
        String token = request.getHeader(Constant.TOKEN);
        log.info("登录用户的X-Token:"+token);

        //通过携带的token获取用户信息
        SecurityUser securityUser = detailsService.getUserByToken(token);

        if (securityUser == null || securityUser.getCurrentUserInfo() == null) {
            CookieUtil.removeCookie(request,response);
            throw new BadCredentialsException("通过header中携带的token获取用户信息失败");
        }
        //操作类型
        securityUser.setType(Constant.PC);

        // 全局注入角色权限信息和登录用户基本信息
        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public String[] supports() {
        return new String[]{Constant.PC};
    }
}