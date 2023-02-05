package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpCarousel;
import com.wehgu.admin.entities.dto.CarouselDTO;
import com.wehgu.admin.entities.query.CarousePageQuery;
import com.wehgu.admin.entities.vo.CarouseVO;
import com.wehgu.admin.service.IMpCarouselService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 轮播图表 前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-26
 */
@RestController
@Api(tags = "轮播图管理-接口")
@RequestMapping("/carousel")
public class MpCarouselController {
    @Resource
    private IMpCarouselService iMpCarouselService;

    @PostMapping(value = "/getCarouselPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取轮播图片", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取轮播图片")
    public ResultTemplate getPostPage(@RequestBody CarousePageQuery input) {

        Page<CarouseVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<CarouseVO> result = iMpCarouselService.selectCarousels(page, input);

        return ResultTemplate.ok(200, "获取轮播图列表成功", result);
    }

    @DeleteMapping(value = "/deleteCarousel", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除轮播图接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除轮播图接口")
    public ResultTemplate deleteCarousel(@RequestParam String UID) {

        EmptyUtil.bool(
                iMpCarouselService.remove(new QueryWrapper<MpCarousel>()
                        .eq("carousel_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/saveCarousel", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存轮播图接口", httpMethod = "POST", response = ResultTemplate.class, notes = "保存轮播图接口")
    public ResultTemplate saveCarousel(@RequestBody CarouselDTO input) {

        MpCarousel carousel=new MpCarousel();
        BeanUtil.copyProperties(input,carousel);
        String UID =input.getCarouselUuid();

        if(StringUtils.isBlank(UID)){
            //新增
            UID= BaseUtil.getUUID();
            carousel.setCarouselUuid(UID);
            EmptyUtil.bool(iMpCarouselService.save(carousel),"新增轮播图记录失败");

        }else {
            EmptyUtil.bool(iMpCarouselService.update(carousel,new QueryWrapper<MpCarousel>()
                    .eq("carousel_uuid",UID)),"编辑轮播图记录失败");
        }


        return ResultTemplate.ok("操作成功",UID);
    }
}
