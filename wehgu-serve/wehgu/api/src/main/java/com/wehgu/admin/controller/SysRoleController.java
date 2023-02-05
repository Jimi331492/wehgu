package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysRolePerm;
import com.wehgu.admin.entities.dto.AuthorizeDTO;
import com.wehgu.admin.entities.dto.RoleDTO;
import com.wehgu.admin.entities.dto.SysRolePermDTO;
import com.wehgu.admin.entities.query.RolePageQuery;
import com.wehgu.admin.entities.vo.RoleVO;
import com.wehgu.admin.service.ISysPermissionService;
import com.wehgu.admin.service.ISysRolePermService;
import com.wehgu.admin.service.ISysRoleService;

import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
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
 * @since 2022-01-24
 */
@RestController
@RequestMapping("/sys_role")
@Api(tags="角色管理-接口")
public class SysRoleController {

    @Resource
    ISysRoleService iSysRoleService;

    @Resource
    ISysRolePermService iSysRolePermService;

    @Resource
    ISysPermissionService iSysPermService;

    @PostMapping(value = "/saveRole", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "新增角色接口", httpMethod = "POST", response = ResultTemplate.class, notes = "新增角色接口")
    public ResultTemplate saveRole(@RequestBody RoleDTO input) {
        SysRole sysRole=new SysRole();
        BeanUtil.copyProperties(input,sysRole);
        String UID =input.getRoleUuid();

        if(StringUtils.isBlank(UID)){
            UID= BaseUtil.getUUID();
            sysRole.setRoleUuid(UID);
            EmptyUtil.bool(
                    iSysRoleService.save(sysRole),"新增角色失败");
        }else{
            sysRole.setRoleUuid(UID);
            EmptyUtil.bool(
                    iSysRoleService.update(sysRole,new QueryWrapper<SysRole>()
                            .eq("role_uuid",sysRole.getRoleUuid())),"修改角色失败");
        }

        return ResultTemplate.ok("操作成功",UID);
    }

    @PostMapping(value = "/getRolePage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取角色列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取角色列表")
    public ResultTemplate getRolePage(@RequestBody RolePageQuery input) {

        Page<RoleVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<RoleVO> result = iSysRoleService.selectRoles(page, input);

        return ResultTemplate.ok("获取角色列表成功", result);
    }



    @PostMapping(value = "/getPermsTree", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取角色的权限列表", httpMethod = "POST", response = ResultTemplate.class, notes = "获取角色的权限列表")
    public ResultTemplate getPermsTreeByRoleUID(@RequestParam String UID) {

        List<SysPermission> list=iSysRolePermService.getPermsTreeByRoleUID(UID);
        EmptyUtil.isEmpty(list,"获取角色的权限列表失败");

        return ResultTemplate.ok("获取角色的权限列表成功", list);
    }

    @PostMapping(value = "/getRoleDefKeys", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取角色的权限列表", httpMethod = "POST", response = ResultTemplate.class, notes = "获取角色的权限列表")
    public ResultTemplate getRoleDefKeys(@RequestParam String UID) {

        List<SysRolePerm> sysRolePermList=iSysRolePermService.list(new QueryWrapper<SysRolePerm>()
        .eq("role_uuid",UID));
        List<Integer> list=new ArrayList<>();
        sysRolePermList.forEach(item->list.add(iSysPermService.getOne(new QueryWrapper<SysPermission>()
                .eq("perm_uuid",item.getPermUuid())).getPermId()));



        return ResultTemplate.ok("获取角色的权限列表成功", list);
    }

    @DeleteMapping(value = "/deleteRole", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除角色接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除角色接口")
    public ResultTemplate deleteRole(@RequestParam String UID) {

        EmptyUtil.bool(
                iSysRoleService.remove(new QueryWrapper<SysRole>()
                        .eq("role_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/authorizeRole", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "角色授权", httpMethod = "POST", response = ResultTemplate.class, notes = "角色授权")
    public ResultTemplate authorizeRole(@RequestBody AuthorizeDTO input) {
        //********  角色授权的逻辑：传来的权限UID和数据库查出来的UID进行比对 多的新增,少的删除，都有的不动

        EmptyUtil.isEmpty(input.getRoleUID(),"角色UID不能为空");
        EmptyUtil.isEmpty(input.getPermUIDList(),"权限列表不能为空");

        //最新上传的权限列表
        List<String> lastestPermIdList=input.getPermUIDList();
        List<String> lastest =new ArrayList<>();

        for (String permId:lastestPermIdList) {
            lastest.add(iSysPermService.getById(permId).getPermUuid());
        }

        //放批量新增的新增的
        List<SysRolePerm> saveList=new ArrayList<>();

        //目前的权限列表 通过循环 remove相同的 就变成已删除的权限列表 然后通过批量删除
        List<SysRolePermDTO> current=iSysRolePermService.getPermsListRoleUID(input.getRoleUID());

        for (String UID: lastest) {
            SysRolePermDTO item=new SysRolePermDTO();
            item.setPermUuid(UID);
            item.setRoleUuid(input.getRoleUID());

            if(current.contains(item)){
                current.remove(item);
            }else{
                SysRolePerm sysRolePerm=new SysRolePerm();
                BeanUtil.copyProperties(item,sysRolePerm);
                saveList.add(sysRolePerm);
            }
        }

        if(saveList.size()>0){
            EmptyUtil.bool(
                    iSysRolePermService.saveBatch(saveList),"授权失败");
        }


        for (SysRolePermDTO remove:current) {
            EmptyUtil.bool(
                    iSysRolePermService.remove(new QueryWrapper<SysRolePerm>()
                            .eq("role_uuid",remove.getRoleUuid())
                            .eq("perm_uuid",remove.getPermUuid())),"取消授权失败");
        }


        return ResultTemplate.ok("操作成功");
    }


}
