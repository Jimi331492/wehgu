package com.wehgu.admin.service;

import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.SysRolePerm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.dto.SysRolePermDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
public interface ISysRolePermService extends IService<SysRolePerm> {

    List<SysPermission> getPermsTreeByRoleUID(String roleUID);

    List<SysRolePermDTO> getPermsListRoleUID(String roleUID);
}
