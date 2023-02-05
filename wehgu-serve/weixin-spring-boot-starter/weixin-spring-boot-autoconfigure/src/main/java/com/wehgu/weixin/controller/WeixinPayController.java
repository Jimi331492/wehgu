package com.wehgu.weixin.controller;

import com.jfinal.core.Controller;
import com.wehgu.weixin.service.IWxPayRefundUpdateDBService;
import com.wehgu.weixin.service.IWxPayUpdateDBService;
import com.wehgu.weixin.service.WxPayService;
import com.wehgu.weixin.utils.SpringBootApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WeixinPayController extends Controller {

    @Resource
    private WxPayService wxPayService;

    public void notify_pay(){
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        IWxPayUpdateDBService wxPayUpdateDBService= SpringBootApplicationContextUtils.getBean(IWxPayUpdateDBService.class);
        if(wxPayUpdateDBService!=null){
            wxPayService.getPayResAndUpdateDb(request,response,wxPayUpdateDBService);
            renderNull();
        }else {
            renderText("error");
        }
    }

    public void notify_refund(){
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        IWxPayRefundUpdateDBService wxPayRefundUpdateDBService=SpringBootApplicationContextUtils.getBean(IWxPayRefundUpdateDBService.class);
        if(wxPayRefundUpdateDBService!=null){
            wxPayService.refundNotify(request,response,wxPayRefundUpdateDBService);
            renderNull();
        }else {
            renderText("error");
        }
    }
}
