package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.PermPageQuery;
import com.wehgu.admin.entities.vo.PermVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
@Mapper
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    Page<SysPermission> selectPerms(Page<SysPermission> page, PermPageQuery input);
}
