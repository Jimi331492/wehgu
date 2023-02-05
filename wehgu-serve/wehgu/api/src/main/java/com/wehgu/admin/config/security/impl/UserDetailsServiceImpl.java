package com.wehgu.admin.config.security.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.security.dto.GlobalUser;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.entities.*;
import com.wehgu.admin.entities.vo.RoleVO;
import com.wehgu.admin.service.*;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <p> 自定义userDetailsService - 登录认证详情 </p>
 */
@Service("UserDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserDetailService sysUserDetailService;

    @Resource
    private ISysRolePermService sysRolePermService;


    /***
     * 根据手机号获取用户信息
     * @param telephone:
     * @return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    //todo 登陆1
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {

        // TODO 表中手机号为唯一值
        SysUserDetail customer;
        SysUser user;
        GlobalUser globalUser;

        if (telephone.equals("admin")) {
            user = sysUserService.getOne(new QueryWrapper<SysUser>()
                    .eq("user_uuid", 1).last("LIMIT 1"));
        } else {
            user = sysUserService.getOne(new QueryWrapper<SysUser>()
                    .eq("telephone", telephone).last("LIMIT 1"));
        }
        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("用户手机号不存在！");
        }
        // 用户存在获取账号信息
        customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                .eq("user_uuid", user.getUserUuid()).last("LIMIT 1"));

        globalUser = setGlobalUser(user, customer);

        // 返回UserDetails实现类
        return new SecurityUser(globalUser);
    }

    /***
     * 根据token获取用户权限与基本信息
     *
     * @param token:
     * @return SecurityUser
     */
    public SecurityUser getUserByToken(String token) {
        SysUserDetail customer;
        SysUser user;
        GlobalUser globalUser = new GlobalUser();
        SysRole sysRole = null;
        try {

            Claims claims = JwtUtil.parseToken(token);   // 获得claims
            log.info("解密Token");
            log.info(claims.toString());

            customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                    .eq("user_uuid", claims.get("userUID")));

            user = sysUserService.getOne(new QueryWrapper<SysUser>()
                    .eq("user_uuid", claims.get("userUID")));

            //获取角色信息
            sysRole = sysRoleService.getOne(new QueryWrapper<SysRole>()
                    .eq("role_uuid", customer.getRoleUuid()));

            globalUser = setGlobalUser(user, customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SysRole> roleList = new ArrayList<>();
        roleList.add(sysRole);

        globalUser.setType(Constant.PC);
        return new SecurityUser(globalUser, roleList, Constant.PC);
    }

    private GlobalUser setGlobalUser(SysUser user, SysUserDetail customer) {
        GlobalUser globalUser = new GlobalUser();

        //用户身份信息
        globalUser.setUserUuid(user.getUserUuid());
        globalUser.setTelephone(user.getTelephone());
        globalUser.setName(user.getName());
        globalUser.setPassword(customer.getPassword());
        globalUser.setCampus(user.getCampus());
        globalUser.setUniversity(user.getUniversity());
        globalUser.setSex(user.getSex());
        globalUser.setStudentNo(user.getStudentNo());
        globalUser.setGrade(user.getGrade());
        globalUser.setCollege(user.getCollege());
        globalUser.setMajor(user.getMajor());
        //微信账号信息
        globalUser.setNickname(customer.getNickname());
        globalUser.setIntroduce(customer.getIntroduce());
        globalUser.setAuthentication(customer.getAuthentication());
        globalUser.setAvatar(customer.getAvatar());
        globalUser.setUserDetailUuid(customer.getUserDetailUuid());
        globalUser.setUnionId(customer.getUnionId());//设置unionId

        //获取部门信息 还没写******先用uuids代替
        globalUser.setDeptName(customer.getDeptUuid());


        //获取角色信息
        SysRole sysRole = sysRoleService.getOne(new QueryWrapper<SysRole>()
                .eq("role_uuid", customer.getRoleUuid()));

        RoleVO roleVO = new RoleVO();
        roleVO.setRoleUuid(sysRole.getRoleUuid());
        roleVO.setRoleName(sysRole.getRoleName());
        roleVO.setDescription(sysRole.getDescription());
        globalUser.setRole(roleVO);

        //获取权限信息
        globalUser.setPermList(sysRolePermService.getPermsTreeByRoleUID(sysRole.getRoleUuid()));

        return globalUser;
    }


    /***
     * 根据unionId获取用户权限与基本信息
     *
     * @param unionId:
     * @return: SecurityUser
     */
    public SecurityUser getUserByUnionId(String unionId) {

        SysUserDetail customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                .eq("union_id", unionId)
                .last("LIMIT 1"));

        //如果该unionId没有绑定账号
        if (customer == null) {
            boolean flag = sysUserService.addUserByUnionId(unionId);
            EmptyUtil.bool(flag, "该微信号注册失败");

            //再查
            customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                    .eq("union_id", unionId)
                    .last("LIMIT 1"));
        }

        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq("user_uuid", customer.getUserUuid())
                .last("LIMIT 1"));


        //获取角色信息
        SysRole sysRole = sysRoleService.getOne(new QueryWrapper<SysRole>()
                .eq("role_uuid", customer.getRoleUuid()));

        GlobalUser globalUser = setGlobalUser(user, customer);

        globalUser.setType(Constant.WE_CHAT_MINI_PROGRAM);

        return new SecurityUser(globalUser, Collections.singletonList(sysRole), Constant.WE_CHAT_MINI_PROGRAM);
    }

    /**
     * 生成Token
     */
    public String createToken(SecurityUser securityUser) {
        // 生成jwt访问令牌
        String token = null;
        try {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("username", securityUser.getCurrentUserInfo().getName());
            claims.put("userUID", securityUser.getCurrentUserInfo().getUserUuid());
            token = JwtUtil.createToken("wehgu", "ismiao", claims);
        } catch (Exception e) {
            log.warn(e.toString());
            log.warn(e.getMessage());
            e.printStackTrace();
        }

        return token;
    }

    /**
     * 根据telephone获取微信用户权限与基本信息
     *
     * @param telephone:
     * @return: SecurityUser
     */
    public SecurityUser getUserByTelephone(String telephone) {
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq("telephone", telephone)
                .last("LIMIT 1"));

        //如果该手机号没有账号
        if (user == null) {
            boolean flag = sysUserService.addUserByTelephone(telephone);
            EmptyUtil.bool(flag, "该手机号注册失败");

            //再查
            user = sysUserService.getOne(new QueryWrapper<SysUser>()
                    .eq("telephone", telephone)
                    .last("LIMIT 1"));
        }

        SysUserDetail customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                .eq("user_uuid", user.getUserUuid())
                .last("LIMIT 1"));


        //获取角色信息
        SysRole sysRole = sysRoleService.getOne(new QueryWrapper<SysRole>()
                .eq("role_uuid", customer.getRoleUuid()));

        GlobalUser globalUser = setGlobalUser(user, customer);

        globalUser.setType(Constant.WE_CHAT_MINI_PROGRAM);

        return new SecurityUser(globalUser, Collections.singletonList(sysRole), Constant.WE_CHAT_MINI_PROGRAM);
    }


}
