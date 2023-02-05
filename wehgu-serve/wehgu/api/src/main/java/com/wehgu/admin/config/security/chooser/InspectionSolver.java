package com.wehgu.admin.config.security.chooser;


import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.utils.MultiReadHttpServletRequest;
import com.wehgu.admin.utils.MultiReadHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public abstract class InspectionSolver {
    public abstract UsernamePasswordAuthenticationToken solve(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response, UserDetailsServiceImpl detailsService);

    public abstract String[] supports();
}
