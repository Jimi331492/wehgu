package com.wehgu.weixin.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.jfinal.weixin.sdk.api.Weixin;
import com.jfinal.wxaapp.api.Wxa;
import com.jfinal.wxaapp.api.WxaTemplate;
import com.jfinal.wxaapp.api.WxaTemplateApi;
import com.wehgu.weixin.common.exception.WebServerInnerException;
import com.wehgu.weixin.entity.TemplateMsg;
import com.wehgu.weixin.wxapp.WxaSubscribeApi;
import com.wehgu.weixin.wxapp.WxaSubscribeMsg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateMsgService {

    public Boolean SendTemplateMsg(String appId, TemplateMsg templateMsg){

        if(StrUtil.isBlank(templateMsg.getTemplate_id())){
            throw new WebServerInnerException("消息模板id不能为空");
        }
        if(StrUtil.isBlank(templateMsg.getTouser())){
            throw new WebServerInnerException("模板消息的接收用户不能为空");
        }
        Weixin weixin;
        if(StrUtil.isBlank(appId)){
            weixin=Weixin.use();
        }else {
            weixin=Weixin.use(appId);
        }
        String jsonStr = JSONUtil.toJsonStr(templateMsg);
        ApiResult apiResult = weixin.call(() -> TemplateMsgApi.send(jsonStr));
        return apiResult.isSucceed();
    }

    public Boolean SendWxaTemplateMsg(String appId, WxaTemplate wxaTemplate){
        if(StrUtil.isBlank(wxaTemplate.getForm_id())){
            throw new WebServerInnerException("触发消息的表单id不能为空");
        }
        if(StrUtil.isBlank(wxaTemplate.getTouser())){
            throw new WebServerInnerException("模板消息的接收用户的openid不能为空");
        }
        if(StrUtil.isBlank(wxaTemplate.getTemplate_id())){
            throw new WebServerInnerException("消息模板id不能为空");
        }
        Wxa wxa;
        if(StrUtil.isBlank(appId)){
            wxa=Wxa.use();
        }else {
            wxa=Wxa.use(appId);
        }
        ApiResult apiResult = wxa.call(() -> WxaTemplateApi.send(wxaTemplate));
        if(!apiResult.isSucceed()){
            log.error("小程序模板消息发送失败");
            log.error("发送的appId是:{}",appId);
            log.error("发送的内容是:{}",JSONUtil.toJsonStr(wxaTemplate));
            log.error("返回的结果信息是:{}",apiResult.getJson());
        }
        return apiResult.isSucceed();
    }


    public Boolean SendSubscribeMsg(String appId, WxaSubscribeMsg wxaSubscribeMsg){
        if(StrUtil.isBlank(wxaSubscribeMsg.getTouser())){
            throw new WebServerInnerException("模板消息的接收用户的openid不能为空");
        }
        if(StrUtil.isBlank(wxaSubscribeMsg.getTemplate_id())){
            throw new WebServerInnerException("消息模板id不能为空");
        }
        Wxa wxa;
        if(StrUtil.isBlank(appId)){
            wxa=Wxa.use();
        }else {
            wxa=Wxa.use(appId);
        }
        ApiResult apiResult = wxa.call(() -> WxaSubscribeApi.send(wxaSubscribeMsg));
        if(!apiResult.isSucceed()){
            log.error("小程序订阅消息发送失败");
            log.error("发送的appId是:{}",appId);
            log.error("发送的内容是:{}",JSONUtil.toJsonStr(wxaSubscribeMsg));
            log.error("返回的结果信息是:{}",apiResult.getJson());
        }
        return apiResult.isSucceed();
    }
}