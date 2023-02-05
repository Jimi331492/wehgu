package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.*;
import com.wehgu.admin.entities.dto.OrderDTO;
import com.wehgu.admin.entities.dto.PackageDTO;
import com.wehgu.admin.entities.dto.StarDTO;
import com.wehgu.admin.entities.query.OrderPageQuery;
import com.wehgu.admin.entities.query.TagPageQuery;
import com.wehgu.admin.entities.vo.OrderVO;
import com.wehgu.admin.entities.vo.TagVO;
import com.wehgu.admin.service.IMpOrderDetailService;
import com.wehgu.admin.service.IMpOrderService;
import com.wehgu.admin.service.IMpPackageService;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@RestController
@RequestMapping("/order")
public class MpOrderController {
    @Resource
    private IMpOrderService iMpOrderService;

    @Resource
    private IMpOrderDetailService iMpOrderDetailService;

    
    @PostMapping(value = "/getOrderPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取订单列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取订单列表")
    public ResultTemplate getOrderPage(@RequestBody OrderPageQuery input) {

        Page<OrderVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<OrderVO> result = iMpOrderService.selectOrders(page, input);

        return ResultTemplate.ok("获取订单列表成功", result);
    }

    @Transactional
    @PostMapping(value = "/saveOrder", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "生成订单接口", httpMethod = "POST", response = ResultTemplate.class, notes = "生成订单接口")
    public ResultTemplate saveOrder(@RequestBody OrderDTO input) {
        MpOrder order=new MpOrder();
        BeanUtil.copyProperties(input,order);
        String UID =input.getOrderUuid();

        if(StringUtils.isBlank(UID)){//新增
            //空校验
            EmptyUtil.isEmpty(input,"参数为空");
            EmptyUtil.isEmpty(input.getUserLocationUuid(),"收货地址为空");
            EmptyUtil.isEmpty(input.getPackageUIDList(),"包裹为空");
            EmptyUtil.isEmpty(input.getDeadlineTime(),"最晚送达时间为空");
            EmptyUtil.isEmpty(input.getPrice(),"价格为空");

            UID= BaseUtil.getUUID();
            order.setOrderUuid(UID);
            order.setPackageNum(input.getPackageUIDList().size());
            order.setOrderStatus(Constant.ORDER_STATUS_PENDING);
            EmptyUtil.bool(iMpOrderService.save(order),"生成订单失败");

            for (String item:input.getPackageUIDList()) {

                MpOrderDetail detail=new MpOrderDetail();
                detail.setPackageUuid(item);
                detail.setOrderUuid(UID);

                EmptyUtil.bool(iMpOrderDetailService.save(detail),"生成订单详情包裹失败");
            }
        }else {
            EmptyUtil.bool(iMpOrderService.update(order,new QueryWrapper<MpOrder>()
                    .eq("order_uuid",UID)),"修改订单失败");
        }


        return ResultTemplate.ok(200, "操作成功",UID);
    }

    @DeleteMapping(value = "/deleteOrder", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除订单接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除订单接口")
    public ResultTemplate deleteOrder(@RequestParam String UID) {

        EmptyUtil.bool(
                iMpOrderService.remove(new QueryWrapper<MpOrder>()
                        .eq("order_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }
}
