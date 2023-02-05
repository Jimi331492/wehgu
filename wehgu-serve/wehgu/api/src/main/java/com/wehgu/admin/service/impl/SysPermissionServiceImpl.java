package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.query.PermPageQuery;
import com.wehgu.admin.entities.vo.PermVO;
import com.wehgu.admin.mapper.SysPermissionMapper;
import com.wehgu.admin.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public Page<SysPermission> selectPerms(Page<SysPermission> page, PermPageQuery input) {
        return sysPermissionMapper.selectPerms(page,input);
    }
}