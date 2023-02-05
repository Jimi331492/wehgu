package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.BaseController;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.dto.CustomerDTO;
import com.wehgu.admin.entities.query.CustomerPageQuery;
import com.wehgu.admin.entities.vo.CustomerVO;
import com.wehgu.admin.service.ISysRoleService;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.OSS.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-05
 */
@Api(tags = "用户详情相关-接口")
@RestController
@RequestMapping("/sys_user_detail")
public class SysUserDetailController extends BaseController {
    @Resource
    private ISysUserDetailService iSysUserDetailService;

    @Resource
    private OSSUtil ossUtil;

    @Resource
    private ISysRoleService iSysRoleService;

    @PostMapping(value = "/getCustomerPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取微信登录用户列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取微信登录用户列表")
    public ResultTemplate getCustomerPage(@RequestBody CustomerPageQuery input) {

        Page<CustomerVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<CustomerVO> result = iSysUserDetailService.selectCustomers(page, input);
        List<CustomerVO> list=result.getRecords();

        for (CustomerVO item:list) {
            //获取头像
            if(item.getAvatar()!=null
                    &&item.getAvatar().startsWith("avatar")){
                boolean exitsObject = ossUtil.isExitsObject(item.getAvatar());
                if(!exitsObject){
                    item.setAvatar("/404.png");
                }
                String URI = ossUtil.generatePresignedUrl(item.getAvatar(), 10);
                item.setAvatar(URI);
            }
        }
        result.setRecords(list);

        return ResultTemplate.ok(200, "获取微信登录用户列表成功", result);
    }

    @DeleteMapping(value = "/deleteCustomer", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除微信用户接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除微信用户接口")
    public ResultTemplate deleteCustomer(@RequestParam String UID) {

        EmptyUtil.bool(
                iSysUserDetailService.remove(new QueryWrapper<SysUserDetail>()
                        .eq("user_detail_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/saveCustomer", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "修改微信用户接口", httpMethod = "POST", response = ResultTemplate.class, notes = "修改微信用户接口")
    public ResultTemplate saveCustomer(@RequestBody CustomerDTO input) {
        SysUserDetail customer=new SysUserDetail();
        BeanUtil.copyProperties(input,customer);
        String detailUID =input.getUserDetailUuid();
        //一般不用
        if(StringUtils.isBlank(detailUID)){
            detailUID= BaseUtil.getUUID();
            customer.setUserDetailUuid(detailUID);
            EmptyUtil.bool(
                    iSysUserDetailService.save(customer),"新增微信用户失败");
        }else{
            //给用户分配角色的时候验证角色UID是否存在的
            if(StringUtils.isNotBlank(input.getRoleUuid())){

                SysRole role=iSysRoleService.getOne(new QueryWrapper<SysRole>()
                .eq("role_uuid",input.getRoleUuid()));

                EmptyUtil.isEmpty(role,"角色不存在");
            }



            EmptyUtil.bool(
                    iSysUserDetailService.update(customer,new QueryWrapper<SysUserDetail>()
                            .eq("user_detail_uuid",customer.getUserDetailUuid())),"修改微信用户失败");
        }


        return ResultTemplate.ok("操作成功",detailUID);
    }
}
