package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.PermPageQuery;
import com.wehgu.admin.entities.vo.PermVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
public interface ISysPermissionService extends IService<SysPermission> {

    Page<SysPermission> selectPerms(Page<SysPermission> page, PermPageQuery input);
}
