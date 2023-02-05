package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.RolePageQuery;
import com.wehgu.admin.entities.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-01-24
 */
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Page<RoleVO> selectRoles(Page<RoleVO> page, RolePageQuery input);
}
