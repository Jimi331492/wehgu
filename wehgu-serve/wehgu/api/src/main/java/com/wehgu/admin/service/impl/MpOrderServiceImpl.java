package com.wehgu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpOrder;
import com.wehgu.admin.entities.MpOrderDetail;
import com.wehgu.admin.entities.MpPackage;
import com.wehgu.admin.entities.query.OrderPageQuery;
import com.wehgu.admin.entities.vo.OrderVO;
import com.wehgu.admin.entities.vo.PackageVO;
import com.wehgu.admin.mapper.MpOrderMapper;
import com.wehgu.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@Service
public class MpOrderServiceImpl extends ServiceImpl<MpOrderMapper, MpOrder> implements IMpOrderService {

    @Resource
    private MpOrderMapper mpOrderMapper;

    @Resource
    private IMpOrderDetailService iMpOrderDetailService;

    @Resource
    private IMpPackageService iMpPackageService;

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public Page<OrderVO> selectOrders(Page<OrderVO> page, OrderPageQuery input) {
        Page<OrderVO> orders=mpOrderMapper.selectOrders(page,input);
        for (OrderVO item:orders.getRecords()) {

            item.setUserInfoVO(iSysUserService.getUserInfoVO(item.getUserDetailUuid()));

            List<MpOrderDetail> detailList=iMpOrderDetailService.list(new QueryWrapper<MpOrderDetail>()
                    .eq("order_uuid",item.getOrderUuid())
                    .last("LIMIT 1"));

            List<PackageVO> packageList=new ArrayList<>();
            for (MpOrderDetail detail:detailList) {
                MpPackage bag=iMpPackageService.getOne(new QueryWrapper<MpPackage>()
                        .eq("package_uuid",detail.getPackageUuid())
                        .last("LIMIT 1"));

                PackageVO packageVO=new PackageVO();
                BeanUtil.copyProperties(bag,packageVO);
                packageList.add(packageVO);
            }



            item.setPackageList(packageList);
        }
        return orders;
    }
}
