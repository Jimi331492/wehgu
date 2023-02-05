package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.RolePageQuery;
import com.wehgu.admin.entities.vo.RoleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-01-24
 */
public interface ISysRoleService extends IService<SysRole> {

    Page<RoleVO> selectRoles(Page<RoleVO> page, RolePageQuery input);
}
