package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.vo.UserInfoVO;
import com.wehgu.admin.entities.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.UserPageQuery;
import com.wehgu.admin.entities.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<UserVO> selectUsers(Page<UserVO> page, UserPageQuery input);
}
