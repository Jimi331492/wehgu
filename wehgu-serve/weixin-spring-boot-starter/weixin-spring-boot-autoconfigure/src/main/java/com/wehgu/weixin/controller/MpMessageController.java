package com.wehgu.weixin.controller;

import com.jfinal.aop.Before;
import com.jfinal.wxaapp.jfinal.WxaMsgController;
import com.jfinal.wxaapp.jfinal.WxaMsgInterceptor;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaMiniProgramPageMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;

public class MpMessageController extends WxaMsgController {


    @Before(WxaMsgInterceptor.class)
    @Override
    public void index(){
        super.index();
        renderNull();
    }


    @Override
    protected void processTextMsg(WxaTextMsg textMsg) {

    }

    @Override
    protected void processImageMsg(WxaImageMsg imageMsg) {

    }

    @Override
    protected void processUserEnterSessionMsg(WxaUserEnterSessionMsg userEnterSessionMsg) {

    }

    @Override
    protected void processWxaMiniProgramPageMsgMsg(WxaMiniProgramPageMsg wxaMiniProgramPageMsg) {

    }
}