package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.BaseController;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.common.vo.UserInfoVO;
import com.wehgu.admin.config.security.dto.SecurityUser;

import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysUser;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.dto.AuthDTO;
import com.wehgu.admin.entities.dto.UserDTO;
import com.wehgu.admin.entities.query.UserPageQuery;
import com.wehgu.admin.entities.vo.UserVO;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-01-24
 */
@Api(tags = "用户相关-接口")
@RestController
@Slf4j
@RequestMapping("/sys_user")
public class SysUserController extends BaseController {



    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysUserDetailService iSysUserDetailService;



    @PostMapping(value = "/getUserPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取用户实名认证信息列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取用户列表")
    public ResultTemplate getUserPage(@RequestBody UserPageQuery input) {

        Page<UserVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<UserVO> result = iSysUserService.selectUsers(page, input);

        return ResultTemplate.ok(200, "获取用户列表成功", result);
    }

    @PostMapping(value = "/saveUser", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "新增用户或实名认证", httpMethod = "POST", response = ResultTemplate.class, notes = "用户实名认证或者用来修改实名信息")
    public ResultTemplate saveUser(@RequestBody UserDTO input) {

        SysUser sysUser=new SysUser();
        BeanUtil.copyProperties(input,sysUser);
        String UID =input.getUserUuid();

        if(StringUtils.isBlank(UID)){
            EmptyUtil.bool(
                    iSysUserService.save(sysUser),"新增用户身份信息失败");
        }else{
            //当用户修改绑定手机号时，先查找该手机号有没有被绑定
            if(StringUtils.isNotBlank(input.getTelephone())){
                SysUser one=iSysUserService.getOne(new QueryWrapper<SysUser>()
                        .eq("telephone",input.getTelephone()));
                //如果one不等于空说明手机号被绑定了
                EmptyUtil.bool(one==null,"该手机号已被绑定");
            }
            EmptyUtil.bool(
                    iSysUserService.update(sysUser,new QueryWrapper<SysUser>()
                            .eq("user_uuid",sysUser.getUserUuid())),"修改信息失败");
        }

        return ResultTemplate.ok("操作成功",UID);
    }

    @DeleteMapping(value = "/deleteUser", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除用户实名认证接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除用户实名认证接口")
    public ResultTemplate deleteUser(@RequestParam String UID) {


         SysUserDetail customer = iSysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                        .eq("user_uuid",UID));

         if(customer!=null){
             log.warn("该用户实名认证信息已被微信绑定，请先删除绑定的微信账号");
         }

        EmptyUtil.bool(
                iSysUserService.remove(new QueryWrapper<SysUser>()
                        .eq("user_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @Transactional
    @PostMapping(value = "/userAuth", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "实名认证", httpMethod = "POST", response = ResultTemplate.class, notes = "用户实名认证或者用来修改实名信息")
    public ResultTemplate userAuth(@RequestBody AuthDTO input) {

        EmptyUtil.isEmpty(input.getName(),"真实姓名为空");
        EmptyUtil.isEmpty(input.getSex(),"性别为空");
        EmptyUtil.isEmpty(input.getStudentNo(),"学号为空");
        EmptyUtil.isEmpty(input.getCampus(),"校区为空");
        EmptyUtil.isEmpty(input.getGrade(),"入学年份为空");
        EmptyUtil.isEmpty(input.getCollege(),"学院为空");
        EmptyUtil.isEmpty(input.getMajor(),"专业为空");
        EmptyUtil.isEmpty(input.getUniversity(),"专业为空");

        String userUID=UserUtil.getLoginUserUID();
        String customerUID=UserUtil.getCurrentCustomerUID();

        EmptyUtil.isEmpty(userUID,"登录用户UID为空");
        EmptyUtil.isEmpty(customerUID,"登录用户UID为空");

        SysUser user=new SysUser();
        SysUserDetail customer=new SysUserDetail();

        BeanUtil.copyProperties(input,user);

        user.setUserUuid(userUID);
        customer.setUserDetailUuid(customerUID);
        customer.setAuthentication(true);


        EmptyUtil.bool(
                iSysUserService.update(user,new QueryWrapper<SysUser>()
                        .eq("user_uuid",user.getUserUuid())),"认证信息失败");

        EmptyUtil.bool(
                iSysUserDetailService.update(customer,new QueryWrapper<SysUserDetail>()
                        .eq("user_detail_uuid",customer.getUserDetailUuid())),"认证信息失败");

        return ResultTemplate.ok("认证成功");
    }




}
