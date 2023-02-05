package com.wehgu.admin.config.security.chooser;

import com.wehgu.admin.config.security.dto.SecurityUser;

import javax.servlet.http.HttpServletRequest;

public abstract class LoginSolver {

    public abstract SecurityUser solve(HttpServletRequest request, SecurityUser securityUser);

    public abstract String[] supports();
}
