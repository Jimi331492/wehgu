package com.wehgu.admin.mapper;

import com.wehgu.admin.entities.SysRolePerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.dto.SysRolePermDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface SysRolePermMapper extends BaseMapper<SysRolePerm> {

    List<SysRolePermDTO> getPermsListRoleUID(String roleUID);
}
