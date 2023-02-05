package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;

import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.dto.BatchSaveOSSDTO;

import com.wehgu.admin.entities.query.OssPageQuery;

import com.wehgu.admin.entities.vo.OssVO;

import com.wehgu.admin.service.ISysOssService;
import com.wehgu.admin.utils.EmptyUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-12
 */
@RestController
@RequestMapping("/sys_oss")
public class SysOssController {
    @Resource
    private ISysOssService iSysOssService;

    @PostMapping(value = "/getOSSPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取图片路径列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取图片路径列表")
    public ResultTemplate getOssPage(@RequestBody OssPageQuery input) {

        Page<OssVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<OssVO> result = iSysOssService.selectOss(page, input);

        return ResultTemplate.ok(200, "获取图片列表成功", result);
    }

    @PostMapping(value = "/getAuthImageGroupByUID", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取图片路径列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取图片路径列表")
    public ResultTemplate getAuthImageGroupByUID(@RequestBody OssPageQuery input) {

        Page<OssVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<OssVO> result = iSysOssService.selectOss(page, input);

        return ResultTemplate.ok(200, "获取图片列表成功", result);
    }

    @DeleteMapping(value = "/deleteOSS", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除路径接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除路径接口")
    public ResultTemplate deleteOSS(@RequestParam String UID) {

        EmptyUtil.bool(
                iSysOssService.remove(new QueryWrapper<SysOss>()
                        .eq("oss_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @Transactional
    @PostMapping(value = "/batchAuditOSS", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "批量审核接口", httpMethod = "POST", response = ResultTemplate.class, notes = "批量审核接口")
    public ResultTemplate batchAuditOSS(@RequestBody BatchSaveOSSDTO input) {

        EmptyUtil.isEmpty(input.getUIDList(),"所选不能为空");

        for (String UID:input.getUIDList()) {
            SysOss oss=new SysOss();
            BeanUtil.copyProperties(input,oss);
            oss.setOssUuid(UID);

            EmptyUtil.bool(
                    iSysOssService.update(oss,new QueryWrapper<SysOss>()
                            .eq("oss_uuid",UID)),"保存失败");
        }

        return ResultTemplate.ok("操作成功");
    }

    @Transactional
    @PostMapping(value = "/batchDeleteOSS", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "批量删除接口", httpMethod = "POST", response = ResultTemplate.class, notes = "批量审核接口")
    public ResultTemplate batchDeleteOSS(@RequestBody List<String> UIDList) {

        EmptyUtil.isEmpty(UIDList,"所选不能为空");

        for (String UID:UIDList) {
            EmptyUtil.bool(
                    iSysOssService.remove(new QueryWrapper<SysOss>()
                            .eq("oss_uuid",UID)),"删除失败");
        }

        return ResultTemplate.ok("操作成功");
    }
}
