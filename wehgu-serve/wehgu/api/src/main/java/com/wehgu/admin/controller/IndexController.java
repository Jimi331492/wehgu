package com.wehgu.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.jfinal.wxaapp.api.WxaUserApi;
import com.wehgu.admin.common.BaseController;
import com.wehgu.admin.common.Constant;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.common.vo.UserInfoVO;
import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.dto.MpLoginDTO;
import com.wehgu.admin.service.ISysOssService;
import com.wehgu.admin.utils.*;
import com.wehgu.admin.utils.OSS.OSSUtil;
import com.wehgu.weixin.utils.WxExtUtil;
import com.wehgu.admin.entities.SysUser;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.dto.RegisterDTO;
import com.wehgu.admin.service.ISysUserDetailService;
import com.wehgu.admin.service.ISysUserService;
import com.wehgu.admin.utils.SMS.TxCloudSmsUtil;
import com.wehgu.admin.utils.SMS.VerifyCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


/**
 * <p> 首页 </p>
 */
@Api(tags = "首页-接口")
@RestController
@Slf4j
public class IndexController extends BaseController {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysUserDetailService iSysUserDetailService;

    @Resource
    private OSSUtil ossUtil;

    @Resource
    private TxCloudSmsUtil txCloudSmsUtil;

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "登录后台管理系统", httpMethod = "POST", response = ResultTemplate.class)
    public void login(@RequestBody String input) { }

    @PostMapping(value = "/SMSLogin", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "小程序手机号一键登录", httpMethod = "POST", response = ResultTemplate.class, notes = "telephoneLogin")
    public ResultTemplate SMSLogin(HttpServletRequest request,@RequestBody MpLoginDTO input) {
        EmptyUtil.isEmpty(input.getSignature(), "上传参数错误");
        EmptyUtil.isEmpty(input.getJscode(), "上传参数错误");
        EmptyUtil.isEmpty(input.getRawData(), "上传参数错误");
        EmptyUtil.isEmpty(input.getIv(), "上传参数错误");
        EmptyUtil.isEmpty(input.getEncryptedData(), "上传参数错误");
        EmptyUtil.isEmpty(input.getCode(), "上传参数错误");
        EmptyUtil.isEmpty(input.getTelephone(), "上传参数错误");

        EmptyUtil.bool(SmsUtil.validatePhoneNumber(input.getTelephone()),"非法的手机号码");
        EmptyUtil.bool(SmsUtil.isValidCode(request, input.getTelephone(), input.getCode()), "验证码不正确或已过期");
        HttpSession session = request.getSession();
        //验证成功后清空Session
        session.removeAttribute("telephone");
        session.removeAttribute("code");
        session.removeAttribute("date");


        log.info("微信端验证码登录 手机号：{}", input.getTelephone());
        log.info("微信端验证码登录 验证码 {}", input.getCode());
        ApiResult apiResult_code = WxaUserApi.getSessionKey(input.getJscode());

        String sessionKey;
        if (apiResult_code.isSucceed()) {
            sessionKey = apiResult_code.get("session_key");
        } else {
            return ResultTemplate.err(apiResult_code.getErrorMsg());
        }

        boolean check = WxaUserApi.checkUserInfo(sessionKey, input.getRawData(), input.getSignature());
        if (!check) {
            return ResultTemplate.err("UserInfo check fail");
        }

        log.info("sessionKey:{}", sessionKey);
        log.info("encryptedData:{}", input.getEncryptedData());
        log.info("iv:{}", input.getIv());

        ApiResult apiResult = WxaUserApi.getUserInfo(sessionKey, input.getEncryptedData(), input.getIv());
        if (!apiResult.isSucceed()) {
            return ResultTemplate.err(apiResult.getErrorMsg());
        }

        //通过手机号获取用户unionId
        SecurityUser securityUser = userDetailsService.getUserByTelephone(input.getTelephone());
        Map<String, Object> map = new HashMap<>();
        String unionId=securityUser.getCurrentUserInfo().getUnionId();
        String openId = apiResult_code.get("openid");
        log.info("unionId:{}", unionId);
        log.info("openId:{}", openId);
        log.info("apiResult {}", apiResult);


        map.put(Constant.WX_SP_UNIONID_HEADER, unionId);
        apiResult_code.getAttrs().put("unionId", unionId);

        String sessionId = BaseUtil.getUUID();
        map.put(Constant.WX_SP_SESSIONID_HEADER, sessionId);


        if (StrUtil.isNotBlank(openId)) {
            map.put(Constant.WX_SP_OPENID_HEADER, openId);
            apiResult_code.getAttrs().put("openId", openId);
        }

        if (StrUtil.isNotBlank(unionId) || StrUtil.isNotBlank(openId)) {
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            String jsonStringNewSessionStr = JSONUtil.toJsonStr(apiResult_code.getAttrs());
            if (StrUtil.isNotBlank(jsonStringNewSessionStr)) {
                accessTokenCache.set(WxExtUtil.getSpSessionIdKey(sessionId), jsonStringNewSessionStr);
            }
        }

        return ResultTemplate.ok(map);
    }


    @PostMapping(value = "/mp_get_unionId", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "小程序获取unionId一键登录", httpMethod = "POST", response = ResultTemplate.class)
    public ResultTemplate getUnionId(@RequestBody MpLoginDTO input) {

        EmptyUtil.isEmpty(input.getSignature(), "上传参数错误");
        EmptyUtil.isEmpty(input.getJscode(), "上传参数错误");
        EmptyUtil.isEmpty(input.getRawData(), "上传参数错误");
        EmptyUtil.isEmpty(input.getIv(), "上传参数错误");
        EmptyUtil.isEmpty(input.getEncryptedData(), "上传参数错误");

        log.info("Jscode {}", input.getJscode());
        //通过jscode获取session_key ApiResult{appId,appSecret,jscode}
        ApiResult apiResult_code = WxaUserApi.getSessionKey(input.getJscode());
        log.info("apiResult_code {}", apiResult_code);

        String sessionKey;
        if (apiResult_code.isSucceed()) {
            sessionKey = apiResult_code.get("session_key");
        } else {
            return ResultTemplate.err(apiResult_code.getErrorMsg());
        }

        boolean check = WxaUserApi.checkUserInfo(sessionKey, input.getRawData(), input.getSignature());
        if (!check) {
            return ResultTemplate.err("UserInfo check fail");
        }
        log.info("sessionKey:{}", sessionKey);
        log.info("encryptedData:{}", input.getEncryptedData());
        log.info("iv:{}", input.getIv());

        ApiResult apiResult = WxaUserApi.getUserInfo(sessionKey, input.getEncryptedData(), input.getIv());
        if (!apiResult.isSucceed()) {
            return ResultTemplate.err(apiResult.getErrorMsg());
        }

        String unionId = apiResult_code.get("unionid");
        String openId = apiResult_code.get("openid");
        log.info("unionId:{}", unionId);
        log.info("openId:{}", openId);
        log.info("apiResult {}", apiResult);
        Map<String, Object> map = new HashMap<>();

        String sessionId = BaseUtil.getUUID();
        map.put(Constant.WX_SP_SESSIONID_HEADER, sessionId);

        if (StrUtil.isNotBlank(unionId)) {
            map.put(Constant.WX_SP_UNIONID_HEADER, unionId);
            apiResult_code.getAttrs().put("unionId", unionId);
        }

        if (StrUtil.isNotBlank(openId)) {
            map.put(Constant.WX_SP_OPENID_HEADER, openId);
            apiResult_code.getAttrs().put("openId", openId);
        }

        if (StrUtil.isNotBlank(unionId) || StrUtil.isNotBlank(openId)) {
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            String jsonStringNewSessionStr = JSONUtil.toJsonStr(apiResult_code.getAttrs());
            if (StrUtil.isNotBlank(jsonStringNewSessionStr)) {
                accessTokenCache.set(WxExtUtil.getSpSessionIdKey(sessionId), jsonStringNewSessionStr);
            }
        }

        //获取用户信息
        SysUserDetail customer = iSysUserDetailService.getOne(new QueryWrapper<SysUserDetail>()
                .eq("union_id",unionId));

        if(customer==null){
            EmptyUtil.bool(iSysUserService.addUserByUnionId(unionId,input.getRawData()),"注册失败");
        }



        return ResultTemplate.ok(map);
    }

    @PostMapping(value = "/getCurrentUserInfo", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "POST", response = ResultTemplate.class, notes = "获取当前登录用户信息")
    public ResultTemplate getCurrentUserInfo(@RequestHeader(name = "X-Token") String token) {
        UserInfoVO userInfoVO = new UserInfoVO();

        //获取用户信息
        SecurityUser securityUser = userDetailsService.getUserByToken(token);

        //复制到userInfoVO
        BeanUtil.copyProperties(securityUser.getCurrentUserInfo(), userInfoVO);


        return ResultTemplate.ok(200, "获取当前登录用户信息成功", userInfoVO);
    }

    @PostMapping(value = "/getMPUserInfo", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取小程序登录用户信息", httpMethod = "POST", response = ResultTemplate.class, notes = "获取当前登录用户信息")
    public ResultTemplate getMPUserInfo(@RequestBody String unionId) {
        UserInfoVO userInfoVO = new UserInfoVO();

        //获取用户信息
        SecurityUser securityUser = userDetailsService.getUserByUnionId(unionId);
        //复制到userInfoVO
        BeanUtil.copyProperties(securityUser.getCurrentUserInfo(), userInfoVO);
        if(userInfoVO.getAvatar()!=null
                &&userInfoVO.getAvatar().startsWith("avatar")){
            boolean exitsObject = ossUtil.isExitsObject(userInfoVO.getAvatar());
            if(!exitsObject){
                return ResultTemplate.err("该图片已不存在");
            }
            String URI = ossUtil.generatePresignedUrl(userInfoVO.getAvatar(), 10);
            userInfoVO.setAvatar(URI);
        }

        return ResultTemplate.ok(200, "获取当前登录用户信息成功", userInfoVO);
    }


    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "用户注册", httpMethod = "POST", response = ResultTemplate.class, notes = "用户注册")
    public ResultTemplate userRegister(@RequestBody RegisterDTO input) {
        EmptyUtil.isEmpty(input.getTelephone(), "请填写手机号");
        EmptyUtil.isEmpty(input.getPassword(), "请填写密码");

        //验证是否存在用户
        SysUser one = iSysUserService.getOne(new QueryWrapper<SysUser>()
                .eq("telephone", input.getTelephone())
                .last("LIMIT 1"));

        EmptyUtil.bool(one == null, "手机号已被注册，请直接登录");

        //验证密码强度
        EmptyUtil.bool(PasswordUtil.isLetterDigit(input.getPassword()), "密码需包含大小写字母及数字且在8-20位");

        if (input.getRoleUID() == null || input.getRoleUID().equals("")) {
            input.setRoleUID(Constant.MP_USER);
        }


        if (input.getDeptUID() == null || input.getRoleUID().equals("")) {
            input.setDeptUID(Constant.MP_DEFAULT_DEPT);
        }



        String UserUID = BaseUtil.getUUID();
        String UserDetailUID = BaseUtil.getUUID();
        SysUser userInfo = new SysUser();
        SysUserDetail accountInfo = new SysUserDetail();
        //保存用户信息
        userInfo.setUserUuid(UserUID);
        userInfo.setTelephone(input.getTelephone());

        EmptyUtil.bool(iSysUserService.save(userInfo), "注册失败");
        //保存账号信息 加密密码存储到数据库
        accountInfo.setPassword(PasswordUtil.encodePassword(input.getPassword()));
        accountInfo.setUserUuid(UserUID);
        accountInfo.setUserDetailUuid(UserDetailUID);
        accountInfo.setRoleUuid(input.getRoleUID());
        accountInfo.setDeptUuid(input.getDeptUID());
        accountInfo.setUnionId(input.getUnionId());

        boolean flag = iSysUserDetailService.save(accountInfo);

        if (!flag) {
            iSysUserService.remove(new QueryWrapper<SysUser>()
                    .eq("user_uuid", userInfo.getUserUuid())
                    .last("LIMIT 1"));
            return ResultTemplate.err(400, "账号注册失败,请重新注册");
        }


        return ResultTemplate.ok(200, "注册成功");
    }


    @GetMapping(value = "/sendSMS", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "发送验证码", httpMethod = "GET", response = ResultTemplate.class, notes = "发送验证码")
    public ResultTemplate sendSMS(@RequestParam(name = "telephone") String telephone, HttpServletRequest request) {

        EmptyUtil.bool(SmsUtil.validatePhoneNumber(telephone),"非法的手机号码");

        HttpSession session = request.getSession();
        session.setAttribute("currentDate", System.currentTimeMillis());//Session放入当前系统时间
        long currentDate = (Long) session.getAttribute("currentDate");
        Long date = (Long) session.getAttribute("date");
        //一分钟内若发送验证码，currentDate的值则变成当前系统时间替换第一次发送验证码存入的时间，并获取第一次的date相减，两次验证码发送时间相隔不超过一分钟，不允许持续发送
        if (date != null && currentDate - date < 1000 * 60) {
            String sessionVerifyCode = String.valueOf(session.getAttribute("code"));
            return ResultTemplate.ok(400, "验证码发送未超过一分钟", sessionVerifyCode);
        }
        // 生成随机的四位数验证码
        String code = VerifyCode.createRandom(true, 4);
        //SmsParams smsParams = new SmsParams(telephone, code);
        //String str = txCloudSmsUtil.sendSms(smsParams);
        //EmptyUtil.bool(str.contains("success"),str);
        logger.info("生成的验证码为：{}", code);


        //第一次发送验证码
        session.setAttribute("date", System.currentTimeMillis());//Session放入当前系统时间
        session.setAttribute("telephone", telephone);//Session放入手机号码
        session.setAttribute("code", code);//Session放入验证码
        return ResultTemplate.ok(200, "发送成功", code);
    }

    @GetMapping(value = "/checkSMS", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "校验验证码", httpMethod = "GET", response = ResultTemplate.class, notes = "校验验证码")
    public ResultTemplate checkSMS(HttpServletRequest request,
                                   @RequestParam(name = "telephone") String telephone,
                                   @RequestParam(name = "code") String code) {

        EmptyUtil.bool(SmsUtil.validatePhoneNumber(telephone),"非法的手机号码");
        EmptyUtil.bool(SmsUtil.isValidCode(request, telephone, code), "验证码不正确或已过期");

        HttpSession session = request.getSession();
        //验证成功后清空Session
        session.removeAttribute("telephone");
        session.removeAttribute("code");
        session.removeAttribute("date");

        return ResultTemplate.ok("校验成功");
    }






}
