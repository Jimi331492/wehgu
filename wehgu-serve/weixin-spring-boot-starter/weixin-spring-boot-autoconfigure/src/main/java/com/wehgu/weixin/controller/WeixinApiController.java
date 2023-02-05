package com.wehgu.weixin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.wehgu.weixin.config.WeiXinNetProperties;
import com.wehgu.weixin.entity.NetVisitEntity;

import javax.annotation.Resource;

public class WeixinApiController extends ApiController {


    @Resource
    private WeiXinNetProperties weiXinNetProperties;

    public void getpara(){
        String appId = getPara("appId");
        if(StrUtil.isBlank(appId)){
            appId="为空";
        }
        renderText(appId);
    }


    public void netprop(){
        NetVisitEntity pay_notify = weiXinNetProperties.getPay_notify();
        NetVisitEntity wx_menu = weiXinNetProperties.getWx_menu();
        String stringBuilder = "支付通知地址信息:" + JSONUtil.toJsonStr(pay_notify) +
                "微信菜单地址前缀信息:" + JSONUtil.toJsonStr(wx_menu);
        renderText(stringBuilder);
    }
}