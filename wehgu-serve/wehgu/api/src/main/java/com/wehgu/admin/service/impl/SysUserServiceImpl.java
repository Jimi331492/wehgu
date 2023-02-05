package com.wehgu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.entities.SysUser;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.query.UserPageQuery;
import com.wehgu.admin.entities.vo.UserInfoVO;
import com.wehgu.admin.entities.vo.UserVO;
import com.wehgu.admin.mapper.SysUserMapper;
import com.wehgu.admin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.utils.BaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleServiceImpl sysRoleService;

    @Resource
    private SysUserDetailServiceImpl sysUserDetailService;


    @Override
    public Page<UserVO> selectUsers(Page<UserVO> page, UserPageQuery input) {

        return sysUserMapper.selectUsers(page, input);
    }

    @Override
    public UserInfoVO getUserInfoVO(String userDetailUuid) {
        SysUserDetail customer = sysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                .eq("user_detail_uuid",userDetailUuid)
                .last("LIMIT 1"));
        SysUser user=getOne(new QueryWrapper<SysUser>()
                .eq("user_uuid",customer.getUserUuid())
                .last("LIMIT 1"));

        UserInfoVO userInfoVO=new UserInfoVO();

        BeanUtil.copyProperties(customer,userInfoVO);
        BeanUtil.copyProperties(user,userInfoVO);
        return userInfoVO;
    }


    @Override
    public boolean addUserByUnionId(String UnionId, String rawData) {
        SysUserDetail customer = new SysUserDetail();
        SysUser user = new SysUser();

        JSONObject jsonRawData = JSONObject.parseObject(rawData);

        String userUID = BaseUtil.getUUID();
        String customerUID = BaseUtil.getUUID();

        String nickname = jsonRawData.getString("nickName");
        String avatar = jsonRawData.getString("avatarUrl");
        Integer gender = jsonRawData.getInteger("gender");



        user.setUserUuid(userUID);
        sysUserMapper.insert(user);

        customer.setUserUuid(userUID);
        customer.setUserDetailUuid(customerUID);
        customer.setNickname(nickname);
        customer.setAvatar(avatar);
        customer.setGender(gender);
        customer.setUnionId(UnionId);
        customer.setRoleUuid(Constant.MP_USER);
        customer.setDeptUuid(Constant.MP_DEFAULT_DEPT);

        return sysUserDetailService.save(customer);
    }

    @Override
    public boolean addUserByUnionId(String UnionId) {
        SysUserDetail customer = new SysUserDetail();
        SysUser user = new SysUser();

        String customerUID = BaseUtil.getUUID();
        String userUID = BaseUtil.getUUID();

        user.setUserUuid(userUID);
        sysUserMapper.insert(user);

        customer.setUserDetailUuid(customerUID);
        customer.setUserUuid(userUID);
        customer.setUnionId(UnionId);
        customer.setRoleUuid(Constant.MP_USER);
        customer.setDeptUuid(Constant.MP_DEFAULT_DEPT);

        return sysUserDetailService.save(customer);
    }

    @Override
    public boolean addUserByTelephone(String telephone) {
        SysUser user = new SysUser();
        SysUserDetail customer = new SysUserDetail();

        String userUID = BaseUtil.getUUID();
        String customerUID = BaseUtil.getUUID();

        user.setUserUuid(userUID);
        user.setTelephone(telephone);

        customer.setUserDetailUuid(customerUID);
        customer.setUnionId(customerUID);//为空不行
        customer.setUserUuid(userUID);
        customer.setRoleUuid(Constant.MP_USER);
        customer.setDeptUuid(Constant.MP_DEFAULT_DEPT);

        sysUserMapper.insert(user);


        return sysUserDetailService.save(customer);
    }


}
