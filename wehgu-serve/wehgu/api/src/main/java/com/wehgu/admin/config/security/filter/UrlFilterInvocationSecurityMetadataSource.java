package com.wehgu.admin.config.security.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wehgu.admin.config.AuthProperties;
import com.wehgu.admin.config.Constants;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysRolePerm;
import com.wehgu.admin.mapper.SysPermissionMapper;
import com.wehgu.admin.mapper.SysRoleMapper;
import com.wehgu.admin.mapper.SysRolePermMapper;
import com.wehgu.admin.utils.UserUtil;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 获取访问该url所需要的用户角色权限信息 </p>
 */
@Component
//todo 第二步，获取用户角色url角色信息，权限赋值到
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private SysPermissionMapper menuMapper;
    @Resource
    private SysRolePermMapper roleMenuMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private AuthProperties myProperties;

    /***
     * 返回该url所需要的用户权限信息
     *
     * @param object: 储存请求url信息
     * @return: null：标识不需要任何权限都可以访问
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取当前请求url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();

        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.split("\\?")[0];
        }

        SecurityUser securityUser = UserUtil.getLoginUser();
        //如果登录用户的角色UID为superadmin直接return
        if (securityUser != null
                && securityUser.getCurrentUserInfo().getRole()!=null
                && securityUser.getCurrentUserInfo().getRole().getRoleName().equals("超级管理员")) {
            return null;
        }

        //微信端
        // TODO 微信端URL请放在此处进行过滤放行
        if (((FilterInvocation) object).getRequest().getHeader("user-agent").toLowerCase().contains("micromessenger")){
            if (myProperties.getAuth().getWeChatUrl().contains(requestUrl)){
                return null;
            }
        }

        // TODO 忽略url请放在此处进行过滤放行
        for (String ignoreUrl : myProperties.getAuth().getIgnoreUrls()) {
            if(requestUrl.equals(ignoreUrl)){
                return null;
            }
        }



        // 数据库中所有url
        List<SysPermission> permissionList = menuMapper.selectList(null);
        String finalRequestUrl = requestUrl;

        List<SysPermission> list = permissionList.stream().filter(x ->
                CollectionUtils.arrayToList(("" + x.getUrl()).split(",")).contains(finalRequestUrl)).collect(Collectors.toList());

        if (list.size() > 0) {
            List<SysRolePerm> rolePermList = roleMenuMapper.selectList(new QueryWrapper<SysRolePerm>()
                    .eq("perm_uuid", list.get(0).getPermUuid()));

            List<String> roles = new LinkedList<>();
            if (!CollectionUtils.isEmpty(rolePermList)) {
                rolePermList.forEach(item -> {
                    String roleUID = item.getRoleUuid();
                    SysRole role = roleMapper.selectOne(new QueryWrapper<SysRole>().eq("role_uuid",roleUID));
                    roles.add(role.getRoleName());
                });
                return SecurityConfig.createList(roles.toArray(new String[0]));
            }
        }

        // 如果数据中没有找到相应url资源则为非法访问，要求用户登录再进行操作
        return SecurityConfig.createList(Constants.ROLE_LOGIN);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}