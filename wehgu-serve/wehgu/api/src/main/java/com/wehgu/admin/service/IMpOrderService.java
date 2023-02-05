package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.OrderPageQuery;
import com.wehgu.admin.entities.vo.OrderVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface IMpOrderService extends IService<MpOrder> {

    Page<OrderVO> selectOrders(Page<OrderVO> page, OrderPageQuery input);
}
