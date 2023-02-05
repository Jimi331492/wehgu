package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysUserDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.CustomerPageQuery;
import com.wehgu.admin.entities.vo.CustomerVO;
import com.wehgu.admin.entities.vo.UserInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
public interface ISysUserDetailService extends IService<SysUserDetail> {


    Page<CustomerVO> selectCustomers(Page<CustomerVO> page, CustomerPageQuery input);


}
