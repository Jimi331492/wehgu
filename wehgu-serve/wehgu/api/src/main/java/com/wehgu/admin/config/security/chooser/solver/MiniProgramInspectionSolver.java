package com.wehgu.admin.config.security.chooser.solver;

import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.security.chooser.InspectionSolver;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.MultiReadHttpServletRequest;
import com.wehgu.admin.utils.MultiReadHttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MiniProgramInspectionSolver extends InspectionSolver {
    @Override
    public UsernamePasswordAuthenticationToken solve(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response, UserDetailsServiceImpl detailsService) {
        log.info("小程序操作");

        String unionId = null;
        String mpSessionId = request.getHeader(Constant.WX_SP_SESSIONID_HEADER);
        log.info("操作的sessionId:"+mpSessionId);
        String mpUnionId = request.getHeader(Constant.WX_SP_UNIONID_HEADER);
        log.info("操作的unionid:"+mpUnionId);
        String appId = request.getHeader("AuthorizationAppWx");
        log.info("操作的appId:"+appId);

        //登录小程序
        EmptyUtil.isEmpty(appId, "小程序appId为空");

        //获取unionId
        if (StringUtils.isNotBlank(mpSessionId)) {
            log.info("登录的sessionId:"+mpSessionId);
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            String sessionJson = accessTokenCache.get(BaseUtil.getSpSessionIdKey(mpSessionId));
            if (StringUtils.isBlank(sessionJson)) {
                //先抛runtime异常
                throw new RuntimeException("token过期");
            }
            ApiResult sessionResult = ApiResult.create(sessionJson);
            unionId = sessionResult.get("unionId");
            if (StringUtils.isBlank(unionId)) {
                throw new RuntimeException("union header is invalid");
            }
        } else if (StringUtils.isNotBlank(mpUnionId)) {
            unionId = mpUnionId;
        }

        //通过unionId获取登录用户
        SecurityUser securityUser = detailsService.getUserByUnionId(unionId);
        if (securityUser == null || securityUser.getCurrentUserInfo() == null) {
            throw new BadCredentialsException("通过unionId获取用户信息失败，请重新登录");
        }

        // 全局注入角色权限信息和登录用户基本信息
        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public String[] supports() {
        return new String[]{Constant.WE_CHAT_MINI_PROGRAM};
    }
}