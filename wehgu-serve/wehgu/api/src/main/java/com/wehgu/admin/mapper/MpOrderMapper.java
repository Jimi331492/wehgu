package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.OrderPageQuery;
import com.wehgu.admin.entities.vo.OrderVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface MpOrderMapper extends BaseMapper<MpOrder> {

    Page<OrderVO> selectOrders(Page<OrderVO> page, OrderPageQuery input);
}
