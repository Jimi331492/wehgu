package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class WxRefundInfo {

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号[必填]
     */
    private String mch_id;

    /**
     * 微信订单号[与商户订单号二选一]
     */
    private String transaction_id;

    /**
     * 商户订单号[与微信订单号二选一]
     */
    private String out_trade_no;

    /**
     * 商户退款单号[必填][内部生成]
     */
    private String out_refund_no;

    /**
     * 订单金额[必填]
     */
    private Integer total_fee;

    /**
     * 退款金额[必填]
     */
    private Integer refund_fee;

    /**
     * 退款原因[选填]
     */
    private String refund_desc;

    /**
     * 退款结果通知url[选填][是否要填,待定]
     */
    private String notify_url;


    public Map<String,String> generateWxRefundApplyMap(String default_notify_url){
        Map<String,String> params=new HashMap<>();
        params.put("appid", this.getAppid());
        params.put("mch_id", this.getMch_id());

        if (StrUtil.isNotBlank(this.getTransaction_id())) {
            params.put("transaction_id", this.getTransaction_id());
        }
        if (StrUtil.isNotBlank(this.getOut_trade_no())) {
            params.put("out_trade_no", this.getOut_trade_no());
        }

        params.put("out_refund_no", this.getOut_refund_no());
        params.put("total_fee", this.getTotal_fee().toString());
        params.put("refund_fee", this.getRefund_fee().toString());

        if (StrUtil.isNotBlank(this.getRefund_desc())) {
            params.put("refund_desc", this.getRefund_desc());
        }

        if (StrUtil.isNotBlank(this.getNotify_url())) {
            params.put("notify_url", this.getNotify_url());
        }else {
            if(StrUtil.isNotBlank(default_notify_url)){
                params.put("notify_url", default_notify_url);
            }
        }
        return params;
    }
}