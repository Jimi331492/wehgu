package com.wehgu.weixin.controller;

import cn.hutool.core.util.StrUtil;

import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.Weixin;
import com.wehgu.weixin.common.BaseController;
import com.wehgu.weixin.utils.WxExtUtil;

public class WxApiController extends BaseController {

    protected Weixin weixin;
    protected String appid;


    @Override
    protected void init(){
        super.init();
        //传递的参数跟jfinal接口保持一致
        //request.getParameter区分大小写
        appid = request.getParameter("appId");
        if(StrUtil.isBlank(appid)){
            weixin= Weixin.use();
            appid= WxExtUtil.getApiConfigKitDefaultAppId();
        }else {
            weixin=Weixin.use(appid);
            ApiConfigKit.setThreadLocalAppId(appid);
        }
    }
}