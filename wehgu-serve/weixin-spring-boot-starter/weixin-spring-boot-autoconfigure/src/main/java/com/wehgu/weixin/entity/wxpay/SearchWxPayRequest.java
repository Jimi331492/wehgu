package com.wehgu.weixin.entity.wxpay;

import lombok.Data;

@Data
public class SearchWxPayRequest {

    /**
     * 微信公众号appid
     */
    private String appid;

    /**
     * 微信商户id
     */
    private String mch_id;

    /**
     * 微信订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;
}