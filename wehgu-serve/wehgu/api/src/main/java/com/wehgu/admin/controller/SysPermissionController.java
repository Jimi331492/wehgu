package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.dto.PermDTO;
import com.wehgu.admin.entities.query.PermPageQuery;
import com.wehgu.admin.entities.vo.PermVO;
import com.wehgu.admin.service.ISysPermissionService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/sys_perm")
@Api(tags="权限管理-接口")
public class SysPermissionController {

    @Resource
    ISysPermissionService iSysPermissionService;

    @PostMapping(value = "/getPermPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取权限列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取权限列表")
    public ResultTemplate getPermPage(@RequestBody PermPageQuery input) {

        Page<SysPermission> page = new Page<>(input.getPage(), input.getLimit());

        Page<SysPermission> result = iSysPermissionService.selectPerms(page, input);

        return ResultTemplate.ok("获取权限列表成功", result);
    }

    @PostMapping(value = "/getPermTree", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取权限树列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取权限树列表")
    public ResultTemplate getCustomerPage(@RequestBody PermPageQuery input) {

        Page<SysPermission> page = new Page<>(input.getPage(), input.getLimit());

        Page<SysPermission> result = iSysPermissionService.selectPerms(page, input);

        //将权限列表转换为树
        List<SysPermission> permTree= UserUtil.permListToTree(result.getRecords());

        SysPermission parent=new SysPermission();
        parent.setParentId(null);
        parent.setPermId(0);
        parent.setType(0);
        parent.setTitle("根目录");
        parent.setChildren(permTree);
        List<SysPermission> perms= new ArrayList<>();

        perms.add(parent);

        result.setRecords(perms);


        return ResultTemplate.ok("获取权限列表成功", result);
    }

    @PostMapping(value = "/savePerm", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "新增权限接口", httpMethod = "POST", response = ResultTemplate.class, notes = "新增权限接口")
    public ResultTemplate savePerm(@RequestBody PermDTO input) {
        SysPermission sysPerm=new SysPermission();
        BeanUtil.copyProperties(input,sysPerm);
        String UID =input.getPermUuid();

        if(StringUtils.isBlank(UID)){
            UID= BaseUtil.getUUID();
            sysPerm.setPermUuid(UID);
            EmptyUtil.bool(
                    iSysPermissionService.save(sysPerm),"新增权限失败");
        }else{
            EmptyUtil.bool(
                    iSysPermissionService.update(sysPerm,new QueryWrapper<SysPermission>()
                            .eq("perm_uuid",sysPerm.getPermUuid())),"修改权限失败");
        }

        return ResultTemplate.ok("操作成功",UID);
    }

    @DeleteMapping(value = "/deletePerm", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除权限接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除权限接口")
    public ResultTemplate deletePerm(@RequestParam String UID) {

        EmptyUtil.bool(
                iSysPermissionService.remove(new QueryWrapper<SysPermission>()
                        .eq("perm_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }




}
