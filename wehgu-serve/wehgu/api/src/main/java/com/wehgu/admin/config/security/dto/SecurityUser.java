package com.wehgu.admin.config.security.dto;

import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.utils.EmptyUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p> 安全认证用户详情 </p>
 */
@Data
@Slf4j
public class SecurityUser implements UserDetails {

    /**
     * 当前登录用户
     */
    private transient GlobalUser currentUserInfo;
    /**
     * 登录类型
     */
    private transient String type;

    /**
     * 角色
     */
    private transient List<SysRole> roleList;

    public SecurityUser(GlobalUser user) {
        EmptyUtil.isEmpty(user, "登录用户为空");
        this.currentUserInfo = user;
    }
    public SecurityUser(GlobalUser user, List<SysRole> sysRole) {
        EmptyUtil.isEmpty(user, "登录用户为空");

        this.currentUserInfo = user;
        this.roleList = sysRole;
    }

    public SecurityUser(GlobalUser globalUser, String type) {

        EmptyUtil.isEmpty(globalUser, "登录用户为空");

        this.currentUserInfo = globalUser;
        this.type = type;
    }

    public SecurityUser(GlobalUser user, List<SysRole> sysRole, String type) {
        EmptyUtil.isEmpty(user, "登录用户为空");
        this.currentUserInfo = user;
        this.roleList = sysRole;
        this.type = type;
    }

    /**
     * 获取当前用户所具有的角色
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (!CollectionUtils.isEmpty(this.roleList)) {
            for (SysRole role : this.roleList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                authorities.add(authority);
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getTelephone();
    }

    public String getEmail() {
        return currentUserInfo.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
