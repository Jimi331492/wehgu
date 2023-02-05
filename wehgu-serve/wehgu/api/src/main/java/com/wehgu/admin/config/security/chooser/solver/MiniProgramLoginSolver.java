package com.wehgu.admin.config.security.chooser.solver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.security.chooser.LoginSolver;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
//todo
public class MiniProgramLoginSolver extends LoginSolver {

    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private ISysUserDetailService userBalanceService;

    @Override
    public SecurityUser solve(HttpServletRequest request, SecurityUser securityUser) {
        log.info("小程序登录");

        String union = null;
        String sessionId = request.getHeader(Constant.WX_SP_SESSIONID_HEADER);
        log.info("登录的sessionId:"+sessionId);

        String unionId = request.getHeader(Constant.WX_SP_UNIONID_HEADER);
        log.info("登录的unionId:"+unionId);

        String appId = request.getHeader("AuthorizationAppWx");
        log.info("登录的appId:"+appId);

        EmptyUtil.isEmpty(appId, "小程序appId为空");

//        String un = securityUser.getUsername() ;
//        log.info("当前登录的微信用户手机号:"+telephone);
//        EmptyUtil.isEmpty(telephone, "手机号不存在");

        //获取unionId
        if (StringUtils.isNotBlank(sessionId)) {
            //获取access_token
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            String sessionJson = accessTokenCache.get(BaseUtil.getSpSessionIdKey(sessionId));

            if (StringUtils.isBlank(sessionJson)) {
                //先抛runtime异常
                throw new RuntimeException("access_token is invalid");
            }

            ApiResult sessionResult = ApiResult.create(sessionJson);
            union = sessionResult.get("unionId");

            if (StringUtils.isBlank(union)) {
                throw new RuntimeException("unionId header is invalid");
            }
        } else if (StringUtils.isNotBlank(unionId)) {
            union = unionId;
        }

        if (StringUtils.isNotBlank(union)) {
            //通过unionId获取用户
            SecurityUser user = userDetailsService.getUserByUnionId(union);
            if (user == null){
                //判断当前是否有unionId绑定这个账户
                SysUserDetail userBalance = userBalanceService.getOne(new QueryWrapper<SysUserDetail>()
                        .eq("user_uuid", securityUser.getCurrentUserInfo().getUserUuid())
                        .last("LIMIT 1"));
                EmptyUtil.isEmpty(userBalance, "该微信用户尚未实名认证");
                if (StringUtils.isBlank(userBalance.getUnionId())){
                    //没有unionId绑定这个账户则将此unionId绑定
                    userBalanceService.update(new UpdateWrapper<SysUserDetail>()
                            .set("union_id", union)
                            .eq("user_uuid", securityUser.getCurrentUserInfo().getUserUuid()));
                }
            }

        }
        else {
            log.info("未找到登录凭证");
            throw new RuntimeException("未找到登录凭证");
        }
        //操作类型
        securityUser.setType(Constant.WE_CHAT_MINI_PROGRAM);
        return securityUser;
    }

    @Override
    public String[] supports() {
        return new String[]{Constant.WE_CHAT_MINI_PROGRAM};
    }
}