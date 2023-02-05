package com.wehgu.admin.config.security.chooser.solver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.security.chooser.LoginSolver;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.service.ISysUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class PCLoginSolver extends LoginSolver {

    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private ISysUserDetailService userBalanceService;
    //todo  一个用户在多用户中，是不是前台需要选择。
    @Override
    public SecurityUser solve(HttpServletRequest request, SecurityUser securityUser) {
        log.info("PC登录");
        List<SysUserDetail> userUID = userBalanceService.list(new QueryWrapper<SysUserDetail>()
                .eq("user_uuid", securityUser.getCurrentUserInfo().getUserUuid()));
        if (userUID == null || userUID.size() == 0) {
            throw new RuntimeException("用户不存在任何信息");
        }

        return securityUser;
    }

    @Override
    public String[] supports() {
        return new String[]{Constant.PC};
    }
}