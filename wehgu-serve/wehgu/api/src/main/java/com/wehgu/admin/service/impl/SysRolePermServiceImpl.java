package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.SysRolePerm;
import com.wehgu.admin.entities.dto.SysRolePermDTO;
import com.wehgu.admin.mapper.SysRolePermMapper;
import com.wehgu.admin.service.ISysPermissionService;
import com.wehgu.admin.service.ISysRolePermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.service.ISysRoleService;
import com.wehgu.admin.utils.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
@Service
public class SysRolePermServiceImpl extends ServiceImpl<SysRolePermMapper, SysRolePerm> implements ISysRolePermService {

    @Resource
    private SysRolePermMapper sysRolePermMapper;

    @Resource
    private ISysPermissionService sysPermissionService;

    @Override
    public List<SysPermission> getPermsTreeByRoleUID(String roleUID) {
        List<SysPermission> permList = new ArrayList<>();

        List<SysRolePerm> rolePermList=sysRolePermMapper.selectList(new QueryWrapper<SysRolePerm>()
                .eq("role_uuid",roleUID)
                .orderByAsc("add_time"));

        for (SysRolePerm sysRolePerm:rolePermList) {
            SysPermission sysPermission=sysPermissionService.getOne(new QueryWrapper<SysPermission>()
                    .eq("perm_uuid",sysRolePerm.getPermUuid()));

            permList.add(sysPermission);
        }


        return UserUtil.permListToTree(permList);
    }

    @Override
    public List<SysRolePermDTO> getPermsListRoleUID(String roleUID) {
        return sysRolePermMapper.getPermsListRoleUID(roleUID);
    }
}
