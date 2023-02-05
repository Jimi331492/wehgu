package com.wehgu.weixin.utils;


import cn.hutool.core.util.StrUtil;


import com.wehgu.weixin.common.exception.WebServerInnerException;
import com.wehgu.weixin.config.WeiXinNetProperties;
import com.wehgu.weixin.entity.NetVisitEntity;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.annotation.Resource;

public class WxExtUtilsWithSpring {

    @Resource
    private ServerProperties serverProperties;

    @Resource
    private WeiXinNetProperties weiXinNetProperties;


    public String getWXMenuPrefix(){
        NetVisitEntity wx_menu = weiXinNetProperties.getWx_menu();
        StringBuilder stringBuilder =new StringBuilder();
        if(StrUtil.isBlank(wx_menu.getSchemes())){
            throw new WebServerInnerException("访问微信菜单使用的协议没有配置");
        }
        stringBuilder.append(wx_menu.getSchemes()).append("://");
        if(StrUtil.isBlank(wx_menu.getIp_or_domain())){
            throw new WebServerInnerException("访问微信菜单必须使用域名");
        }
        stringBuilder.append(wx_menu.getIp_or_domain());
        if(wx_menu.getPort()!=80){
            throw new WebServerInnerException("访问微信菜单必须使用80端口");
        }

        if(StrUtil.isNotBlank(wx_menu.getSerlet_context())){
            if(!wx_menu.getSerlet_context().startsWith("/")){
                throw new WebServerInnerException("自定义serverlt_context必须以/开头");
            }
            if(wx_menu.getSerlet_context().trim().length()==1){
                throw new WebServerInnerException("自定义serverlt_context无效");
            }
            stringBuilder.append(wx_menu.getSerlet_context());
        }//else {
//            if(StrUtil.isNotBlank(serverProperties.getServlet().getContextPath())){
//                stringBuilder.append(serverProperties.getServlet().getContextPath());
//            }
//        }
        return stringBuilder.toString();
    }

    public String getPayNotifyUrl(){
        StringBuilder stringBuilder =new StringBuilder();
        NetVisitEntity pay_notify = weiXinNetProperties.getPay_notify();
        if(StrUtil.isBlank(pay_notify.getSchemes())){
            throw new WebServerInnerException("访问微信支付通知地址使用的协议没有配置");
        }
        stringBuilder.append(pay_notify.getSchemes()).append("://");
        if(StrUtil.isBlank(pay_notify.getIp_or_domain())){
            throw new WebServerInnerException("访问微信支付通知地址必须使用域名或ip地址");
        }
        stringBuilder.append(pay_notify.getIp_or_domain());
        if(pay_notify.getPort()==null){
            throw new WebServerInnerException("访问微信支付通知地址使用的端口必须有效");
        }
        if(pay_notify.getPort()!=80){
            stringBuilder.append(":").append(pay_notify.getPort());
        }
        if(StrUtil.isNotBlank(pay_notify.getSerlet_context())){
            if(!pay_notify.getSerlet_context().startsWith("/")){
                throw new WebServerInnerException("自定义serverlt_context必须以/开头");
            }
            if(pay_notify.getSerlet_context().length()==1){
                throw new WebServerInnerException("自定义serverlt_context无效");
            }
            stringBuilder.append(pay_notify.getSerlet_context());
        }else {
            if(StrUtil.isNotBlank(serverProperties.getServlet().getContextPath())){
                stringBuilder.append(serverProperties.getServlet().getContextPath());
            }
        }
        if(!weiXinNetProperties.getPay_notify_relative_path().startsWith("/")){
            throw new WebServerInnerException("支付结果通知地址必须以/开头");
        }
        if(weiXinNetProperties.getPay_notify_relative_path().trim().length()==1){
            throw new WebServerInnerException("支付结果通知地址无效");
        }
        stringBuilder.append(weiXinNetProperties.getPay_notify_relative_path());
        return stringBuilder.toString();
    }

    public String getRefundNotifyUrl(){
        StringBuilder stringBuilder =new StringBuilder();
        NetVisitEntity pay_notify = weiXinNetProperties.getPay_notify();
        if(StrUtil.isBlank(pay_notify.getSchemes())){
            throw new WebServerInnerException("访问微信退款通知地址使用的协议没有配置");
        }
        stringBuilder.append(pay_notify.getSchemes()).append("://");
        if(StrUtil.isBlank(pay_notify.getIp_or_domain())){
            throw new WebServerInnerException("访问微信退款通知地址必须使用域名或ip地址");
        }
        stringBuilder.append(pay_notify.getIp_or_domain());
        if(pay_notify.getPort()==null){
            throw new WebServerInnerException("访问微信退款通知地址使用的端口必须有效");
        }
        if(pay_notify.getPort()!=80){
            stringBuilder.append(":").append(pay_notify.getPort());
        }
        if(StrUtil.isNotBlank(pay_notify.getSerlet_context())){
            if(!pay_notify.getSerlet_context().startsWith("/")){
                throw new WebServerInnerException("自定义serverlt_context必须以/开头");
            }
            if(pay_notify.getSerlet_context().length()==1){
                throw new WebServerInnerException("自定义serverlt_context无效");
            }
            stringBuilder.append(pay_notify.getSerlet_context());
        }else {
            if(StrUtil.isNotBlank(serverProperties.getServlet().getContextPath())){
                stringBuilder.append(serverProperties.getServlet().getContextPath());
            }
        }
        if(!weiXinNetProperties.getPay_notify_relative_path().startsWith("/")){
            throw new WebServerInnerException("退款结果通知地址必须以/开头");
        }
        if(weiXinNetProperties.getPay_notify_relative_path().trim().length()==1){
            throw new WebServerInnerException("退款结果通知地址无效");
        }
        stringBuilder.append(weiXinNetProperties.getRefund_notify_relative_path());
        return stringBuilder.toString();
    }
}