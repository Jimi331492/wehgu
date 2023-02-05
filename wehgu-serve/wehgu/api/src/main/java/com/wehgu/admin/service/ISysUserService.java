package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.UserPageQuery;
import com.wehgu.admin.entities.vo.UserInfoVO;
import com.wehgu.admin.entities.vo.UserVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
public interface ISysUserService extends IService<SysUser> {

    Page<UserVO> selectUsers(Page<UserVO> page, UserPageQuery input);

    UserInfoVO getUserInfoVO(String userDetailUuid);

    boolean addUserByUnionId(String UnionId,String rawData);

    boolean addUserByUnionId(String UnionId);

    boolean addUserByTelephone(String telephone);
}
