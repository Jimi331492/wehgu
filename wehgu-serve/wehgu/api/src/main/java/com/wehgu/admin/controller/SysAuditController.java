package com.wehgu.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.dto.CustomerDTO;
import com.wehgu.admin.entities.query.OSSAuthPageQuery;
import com.wehgu.admin.entities.query.OssPageQuery;
import com.wehgu.admin.entities.vo.AuthVO;
import com.wehgu.admin.entities.vo.OssVO;
import com.wehgu.admin.service.ISysOssService;
import com.wehgu.admin.service.ISysRoleService;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.service.ISysUserService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p> 首页 </p>
 */
@Api(tags = "审核相关-接口")
@RestController
@Slf4j
@RequestMapping("/audit")
public class SysAuditController {

    @Resource
    private ISysOssService iSysOssService;

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysRoleService iSysRoleService;

    @Resource
    private ISysUserDetailService iSysUserDetailService;

    @PostMapping(value = "/getAuthList", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取认证审核列表", httpMethod = "POST", response = ResultTemplate.class, notes = "获取认证审核列表")
    public ResultTemplate getAuthList(@RequestBody OSSAuthPageQuery input) {


        List<AuthVO> result = iSysOssService.selectOssAuth(input);

        return ResultTemplate.ok(200, "获取认证审核列表成功", result);
    }

    @Transactional
    @PostMapping(value = "/batchAuthSuccess", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "后台认证审核通过", httpMethod = "POST", response = ResultTemplate.class, notes = "后台认证审核通过")
    public ResultTemplate AuthSuccess(@RequestBody List<String> UIDList) {
            EmptyUtil.isEmpty(UIDList,"参数为空");
        for (String UID:UIDList) {
            SysUserDetail customer=new SysUserDetail();
            customer.setRoleUuid(Constant.MP_STUDENT);
            customer.setUserDetailUuid(UID);

            EmptyUtil.bool(
                    iSysUserDetailService.update(customer,new QueryWrapper<SysUserDetail>()
                            .eq("user_detail_uuid",customer.getUserDetailUuid())),"操作失败");
        }



        return ResultTemplate.ok("操作成功");
    }
}
