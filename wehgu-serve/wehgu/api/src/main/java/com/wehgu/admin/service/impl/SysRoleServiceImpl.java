package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.query.RolePageQuery;
import com.wehgu.admin.entities.vo.RoleVO;
import com.wehgu.admin.mapper.SysRoleMapper;
import com.wehgu.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-01-24
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    SysRoleMapper sysRoleMapper;

    @Override
    public Page<RoleVO> selectRoles(Page<RoleVO> page, RolePageQuery input) {
        return sysRoleMapper.selectRoles(page,input);
    }
}
