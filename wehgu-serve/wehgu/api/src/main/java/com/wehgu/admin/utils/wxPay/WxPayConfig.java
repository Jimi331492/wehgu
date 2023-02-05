package com.wehgu.admin.utils.wxPay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置类
 */
@Component
public class WxPayConfig {
    //小程序appid
    public static  String appid = "wxeb4f620b577ff31a";
    //微信支付的商户id
    public static  String mch_id = "1617816020";//商户号
    //微信支付的商户密钥
    public static  String key = "fsdgegwe24EDGFDGgegfetrfrwefger2";//商户的key
    //支付成功后的服务器回调url,回调函数的地址（就是自己写在Controller里的回调函数）但是不许外网能访问（可以进行内网穿透）
    public static  String notify_url = "https://localhost:10077/wehgu/wx/wxNotify";
//    public static  String notify_url = "https://serve.my3iao.com/wehgu/wx/wxNotify";
    //签名方式
    public static  String SIGNTYPE = "MD5";
    //交易类型
    public static  String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static  String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //AppSecret(小程序密钥)
    public static  String app_secret ="ccc721049ed536507b128e9b3b94c3a6";

    @Value("${weixin.config.wxPayConfig.appId}")
    public  void setAppid(String appid) {
        WxPayConfig.appid = appid;
    }
    @Value("${weixin.config.wxPayConfig.machId}")
    public  void setMch_id(String mch_id) {
        WxPayConfig.mch_id = mch_id;
    }
    @Value("${weixin.config.wxPayConfig.machSecret}")
    public  void setKey(String key) {
        WxPayConfig.key = key;
    }
    @Value("${weixin.config.wxPayConfig.notifyUrl}")
    public  void setNotify_url(String notify_url) {
        WxPayConfig.notify_url = notify_url;
    }
    @Value("${weixin.config.wxPayConfig.appSecret}")
    public  void setApp_secret(String app_secret) {
        WxPayConfig.app_secret = app_secret;
    }
}