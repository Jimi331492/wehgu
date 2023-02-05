package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpOrder;
import com.wehgu.admin.entities.MpOrderDetail;
import com.wehgu.admin.entities.MpPackage;
import com.wehgu.admin.entities.dto.OrderDTO;
import com.wehgu.admin.entities.dto.PackageDTO;
import com.wehgu.admin.entities.query.OrderPageQuery;
import com.wehgu.admin.entities.query.PackagePageQuery;
import com.wehgu.admin.entities.vo.OrderVO;
import com.wehgu.admin.entities.vo.PackageVO;
import com.wehgu.admin.service.IMpPackageService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.rmi.server.UID;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@RestController
@RequestMapping("/package")
public class MpPackageController {

    @Resource
    private IMpPackageService iMpPackageService;

    @PostMapping(value = "/getPackagePage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取包裹列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取包裹列表")
    public ResultTemplate getPackagePage(@RequestBody PackagePageQuery input) {

        Page<PackageVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<PackageVO> result = iMpPackageService.selectPackages(page, input);

        return ResultTemplate.ok("获取包裹列表成功", result);
    }

    @Transactional
    @PostMapping(value = "/savePackage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "新增包裹接口", httpMethod = "POST", response = ResultTemplate.class, notes = "生成订单接口")
    public ResultTemplate savePackage   (@RequestBody PackageDTO input) {
        MpPackage mpPackage=new MpPackage();
        BeanUtil.copyProperties(input,mpPackage);
        String UID =input.getPackageUuid();

        if(StringUtils.isBlank(UID)){//新增
                //空校验
                EmptyUtil.isEmpty(input.getPickUpCode(),"取件码为空");
                EmptyUtil.isEmpty(input.getPickUpSite(),"驿站地址为空");

                UID=BaseUtil.getUUID();
                mpPackage.setPackageUuid(UID);
                mpPackage.setUserDetailUuid(UserUtil.getCurrentCustomerUID());
                EmptyUtil.bool(iMpPackageService.save(mpPackage),"添加包裹失败");

        }else {
            EmptyUtil.bool(iMpPackageService.update(mpPackage,new QueryWrapper<MpPackage>()
                    .eq("package_uuid",UID)),"修改包裹失败");
        }


        return ResultTemplate.ok(200, "操作成功",UID);
    }

    @Transactional
    @DeleteMapping(value = "/batchDeletePackage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "批量删除包裹接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "批量删除包裹接口")
    public ResultTemplate deletePackage(@RequestBody List<String> UIDList) {

        EmptyUtil.isEmpty(UIDList,"参数为空");

        for (String item:UIDList) {
            EmptyUtil.bool(
                    iMpPackageService.remove(new QueryWrapper<MpPackage>()
                            .eq("package_uuid",item)),"删除失败");
        }


        return ResultTemplate.ok("删除成功");
    }

}
