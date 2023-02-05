package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysUserDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.CustomerPageQuery;
import com.wehgu.admin.entities.vo.CustomerVO;
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
public interface SysUserDetailMapper extends BaseMapper<SysUserDetail> {

    Page<CustomerVO> selectCustomers(Page<CustomerVO> page, CustomerPageQuery input);
}
