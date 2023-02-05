package com.wehgu.weixin.config;

import com.wehgu.weixin.entity.NetVisitEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "weixin.netvisit.config")
public class WeiXinNetProperties {

    /**
     * 微信菜单访问地址信息
     */
    private NetVisitEntity wx_menu;
    /**
     * 微信支付相关通知地址信息
     */
    private NetVisitEntity pay_notify;

    /**
     * 微信支付结果通知相对项目的通知地址
     */
    private String pay_notify_relative_path="/weixin/pay/notify_pay";

    /**
     * 微信退款结果通知相对项目的通知地址
     */
    private String refund_notify_relative_path="/weixin/pay/notify_refund";
}