package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpCarousel;
import com.wehgu.admin.entities.MpNotice;
import com.wehgu.admin.entities.dto.CarouselDTO;
import com.wehgu.admin.entities.dto.NoticeDTO;
import com.wehgu.admin.entities.query.CarousePageQuery;
import com.wehgu.admin.entities.query.NoticePageQuery;
import com.wehgu.admin.entities.vo.CarouseVO;
import com.wehgu.admin.entities.vo.NoticeVO;
import com.wehgu.admin.service.IMpNoticeMarkService;
import com.wehgu.admin.service.IMpNoticeService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/notice")
public class MpNoticeController {

    @Resource
    private IMpNoticeService iMpNoticeService;



    @PostMapping(value = "/getNoticePage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取公告", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取公告")
    public ResultTemplate getNoticePage(@RequestBody NoticePageQuery input) {

        Page<NoticeVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<NoticeVO> result = iMpNoticeService.selectNotices(page, input);

        return ResultTemplate.ok(200, "获取轮播图列表成功", result);
    }

    @DeleteMapping(value = "/deleteNotice", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除公告接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除公告接口")
    public ResultTemplate deleteNotice(@RequestParam String UID) {

        EmptyUtil.bool(
                iMpNoticeService.remove(new QueryWrapper<MpNotice>()
                        .eq("notice_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/saveNotice", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存公告接口", httpMethod = "POST", response = ResultTemplate.class, notes = "保存轮播图接口")
    public ResultTemplate saveNotice(@RequestBody NoticeDTO input) {

        MpNotice notice=new MpNotice();
        BeanUtil.copyProperties(input,notice);
        String UID =input.getNoticeUuid();

        if(StringUtils.isBlank(UID)){
            //新增
            UID= BaseUtil.getUUID();
            notice.setNoticeUuid(UID);
            EmptyUtil.bool(iMpNoticeService.save(notice),"新增公告失败");

        }else {
            EmptyUtil.bool(iMpNoticeService.update(notice,new QueryWrapper<MpNotice>()
                    .eq("notice_uuid",UID)),"保存公告失败");
        }


        return ResultTemplate.ok("操作成功",UID);
    }
}
