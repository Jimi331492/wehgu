package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpPackage;
import com.wehgu.admin.entities.MpUserLocation;
import com.wehgu.admin.entities.dto.LocationDTO;
import com.wehgu.admin.entities.dto.PackageDTO;
import com.wehgu.admin.entities.query.LocationPageQuery;
import com.wehgu.admin.entities.query.PackagePageQuery;
import com.wehgu.admin.entities.vo.LocationVO;
import com.wehgu.admin.entities.vo.PackageVO;
import com.wehgu.admin.service.IMpUserLocationService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.stream.Location;
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
@RequestMapping("/user_location")
public class MpUserLocationController {

    @Resource
    private IMpUserLocationService iMpUserLocationService;

    @PostMapping(value = "/getLocationPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取用户收货地址列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取用户收货地址列表")
    public ResultTemplate getLocationPage(@RequestBody LocationPageQuery input) {

        Page<LocationVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<LocationVO> result = iMpUserLocationService.selectLocations(page, input);

        return ResultTemplate.ok("获取用户地址列表成功", result);
    }

    @Transactional
    @PostMapping(value = "/saveLocation", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "新增用户地址接口", httpMethod = "POST", response = ResultTemplate.class, notes = "新增用户地址接口")
    public ResultTemplate saveLocation(@RequestBody LocationDTO input) {
        MpUserLocation location=new MpUserLocation();
        BeanUtil.copyProperties(input,location);
        String UID =input.getUserLocationUuid();

        if(StringUtils.isBlank(UID)){//新增
            //空校验
            EmptyUtil.isEmpty(input.getDeliveryLocation(),"送货地址为空");
            EmptyUtil.isEmpty(input.getConsigneeTelephone(),"联系人电话为空");
            EmptyUtil.isEmpty(input.getConsignee(),"联系人为空");
            UID= BaseUtil.getUUID();
            location.setUserLocationUuid(UID);
            location.setUserDetailUuid(UserUtil.getCurrentCustomerUID());
            EmptyUtil.bool(iMpUserLocationService.save(location),"添加地址失败");

        }else {
            EmptyUtil.bool(iMpUserLocationService.update(location,new QueryWrapper<MpUserLocation>()
                    .eq("user_location_uuid",UID)),"修改用户地址失败");
        }


        return ResultTemplate.ok(200, "操作成功",UID);
    }

    @Transactional
    @DeleteMapping(value = "/deleteLocation", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除用户地址接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除用户地址接口")
    public ResultTemplate deleteLocation(@RequestParam String UID) {

        EmptyUtil.isEmpty(UID,"参数为空");

        EmptyUtil.bool(
                iMpUserLocationService.remove(new QueryWrapper<MpUserLocation>()
                        .eq("user_location_uuid",UID)),"删除失败");


        return ResultTemplate.ok("删除成功");
    }

}
