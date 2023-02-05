package com.wehgu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysUser;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.query.CustomerPageQuery;
import com.wehgu.admin.entities.vo.CustomerVO;
import com.wehgu.admin.entities.vo.UserInfoVO;
import com.wehgu.admin.mapper.SysUserDetailMapper;
import com.wehgu.admin.service.ISysUserDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
@Service
public class SysUserDetailServiceImpl extends ServiceImpl<SysUserDetailMapper, SysUserDetail> implements ISysUserDetailService {

    @Resource
    private SysUserDetailMapper sysUserDetailMapper;



    @Override
    public Page<CustomerVO> selectCustomers(Page<CustomerVO> page, CustomerPageQuery input) {
        return sysUserDetailMapper.selectCustomers(page,input);
    }


}
