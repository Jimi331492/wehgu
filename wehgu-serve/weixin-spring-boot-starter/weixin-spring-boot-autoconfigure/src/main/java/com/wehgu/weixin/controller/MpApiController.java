package com.wehgu.weixin.controller;

import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.Wxa;
import com.wehgu.weixin.common.BaseController;
import com.wehgu.weixin.utils.WxExtUtil;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;

@Slf4j
public class MpApiController extends BaseController {

    protected Wxa wxa;
    protected String appid;


    @Override
    protected void init(){
        super.init();

        String appId = WxaConfigKit.getAppId();
        log.info("当前线程使用的appId是{}",appId);


        //传递的参数跟jfinal接口保持一致
        //request.getParameter区分大小写
        appid = request.getParameter("appId");
        log.info("接收到的appId是{}",appid);
        if(StrUtil.isBlank(appid)){
            wxa = Wxa.use();
            appid= WxExtUtil.getWxaConfigKitDefaultAppId();
        }else {
            wxa=Wxa.use(appid);
            WxaConfigKit.setThreadLocalAppId(appid);
            appId = WxaConfigKit.getAppId();
            log.info("变更后,当前线程使用的appId是{}",appId);
        }
    }
}