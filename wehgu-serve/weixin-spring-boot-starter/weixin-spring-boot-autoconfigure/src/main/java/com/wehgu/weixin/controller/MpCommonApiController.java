package com.wehgu.weixin.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;

import com.jfinal.wxaapp.api.WxaUserApi;


import com.wehgu.weixin.common.Constant;
import com.wehgu.weixin.common.ResultTemplate;
import com.wehgu.weixin.service.TemplateMsgService;
import com.wehgu.weixin.utils.WxExtUtil;
import com.wehgu.weixin.wxapp.WxaSubscribeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mp_common_api")
public class MpCommonApiController extends MpApiController {

//    private final String url="https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";



    /**
     * 小程序登录 将会话信息保存到当前程序种
     * @param jscode wx.login后返回的jscode
     * @return 返回内置的登录后的sessionId,为鉴权和获取unionId做准备,这一步不一定能获取到unionId
     */
    @PostMapping(value = "sp_login")
    public ResultTemplate sp(@RequestParam(value = "jscode",required = true)String jscode){
        if(StrUtil.isBlank(jscode)){
            return ResultTemplate.err("jscode不能为空");
        }
        ApiResult apiResult = WxaUserApi.getSessionKey(jscode);
        if(apiResult.isSucceed()){
            // 利用 appId 与 accessToken 建立关联，支持多账户
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            String sessionId = UUID.fastUUID().toString(true);
            accessTokenCache.set(WxExtUtil.getSpSessionIdKey(sessionId), apiResult.getJson());
            Map<String,Object> map=new HashMap<>();
            map.put(Constant.WX_SP_SESSIONID_HEADER,sessionId);
            return ResultTemplate.ok(map);
        }else {
            return ResultTemplate.err(apiResult.getErrorMsg());
        }
    }


    /**
     * 通过小程序传递过来的相关信息解密获取unionId
     * @param signature 微信服务器返回给小程序客户端的参数signature
     * @param rawData 微信服务器返回给小程序客户端的参数rawData
     * @param encryptedData  微信服务器返回给小程序客户端的参数encryptedData
     * @param iv 微信服务器返回给小程序客户端的参数iv
     * @return unionId
     */
    @PostMapping(value = "sp_get_unionId")
    public ResultTemplate getUserInfoBySpContext(
            @RequestParam(value = "signature",required = true)String signature,
            @RequestParam(value = "rawData",required = true)String rawData,
            @RequestParam(value = "encryptedData",required = true)String encryptedData,
            @RequestParam(value = "iv",required = true)String iv
    ){
        if(StrUtil.isBlank(signature)){
            return ResultTemplate.err("signature 无效");
        }
        if(StrUtil.isBlank(rawData)){
            return ResultTemplate.err("rawData 无效");
        }
        if(StrUtil.isBlank(encryptedData)){
            return ResultTemplate.err("encryptedData 无效");
        }
        if(StrUtil.isBlank(iv)){
            return ResultTemplate.err("iv 无效");
        }

        // 参数空校验 不做演示
        // 利用 appId 与 accessToken 建立关联，支持多账户
        IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
        String sessionId = request.getHeader(Constant.WX_SP_SESSIONID_HEADER);
        if (StrUtil.isBlank(sessionId)) {
            return ResultTemplate.err("wxaSessionId Header is blank");
        }
        String sessionJson = accessTokenCache.get(WxExtUtil.getSpSessionIdKey(sessionId));

        ApiResult sessionResult = ApiResult.create(sessionJson);
        // 获取sessionKey
        String sessionKey = sessionResult.get("session_key");
        if (StrUtil.isBlank(sessionKey)) {
            return ResultTemplate.err("sessionKey is blank");
        }
        // 用户信息校验
        boolean check = WxaUserApi.checkUserInfo(sessionKey, rawData, signature);
        if (!check) {
            return ResultTemplate.err("UserInfo check fail");
        }

        // 服务端解密用户信息
        ApiResult apiResult = WxaUserApi.getUserInfo(sessionKey, encryptedData, iv);
        if (!apiResult.isSucceed()) {
            return ResultTemplate.err(apiResult.getErrorMsg());
        }
        String unionId=apiResult.get("unionId");
        if(StrUtil.isNotBlank(unionId)){
            sessionResult.getAttrs().put("unionId",unionId);
            String jsonString_newsessionstr = JSONUtil.toJsonStr(sessionResult.getAttrs());
            if(StrUtil.isNotBlank(jsonString_newsessionstr)){
                accessTokenCache.set(WxExtUtil.getSpSessionIdKey(sessionId),jsonString_newsessionstr);
            }
        }
        return ResultTemplate.ok(unionId);
    }




    @Resource
    private TemplateMsgService templateMsgService;

    @PostMapping(value = "sp_subscribemsg_send")
    public ResultTemplate sendTemplateMsgWxa(@RequestBody WxaSubscribeMsg wxaSubscribeMsg){
        Boolean aBoolean = templateMsgService.SendSubscribeMsg(appid,wxaSubscribeMsg);
        if(aBoolean){
            return ResultTemplate.ok("消息发送成功");
        }else {
            return ResultTemplate.err("消息发送失败");
        }
    }







}