package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.Map;

@Data
public class RefundNotifyRes {


    public RefundNotifyRes(){

    }

    public RefundNotifyRes(Map<String,String> ming_w_map,Map<String,String> mi_w_map){
        /**
         * 明文信息赋值
         */
        if(ming_w_map.containsKey("return_code")){
            this.return_code=ming_w_map.get("return_code");
        }
        if(ming_w_map.containsKey("return_msg")){
            this.return_msg=ming_w_map.get("return_msg");
        }
        if(ming_w_map.containsKey("nonce_str")){
            this.nonce_str=ming_w_map.get("nonce_str");
        }
        if(ming_w_map.containsKey("req_info")){
            this.nonce_str=ming_w_map.get("req_info");
        }

        /**
         * 密文信息赋值
         */
        if(mi_w_map.containsKey("transaction_id")){
            this.transaction_id=mi_w_map.get("transaction_id");
        }
        if(mi_w_map.containsKey("out_trade_no")){
            this.out_trade_no=mi_w_map.get("out_trade_no");
        }
        if(mi_w_map.containsKey("out_refund_no")){
            this.out_refund_no=mi_w_map.get("out_refund_no");
        }
        if(mi_w_map.containsKey("refund_id")){
            this.refund_id=mi_w_map.get("refund_id");
        }

        if(mi_w_map.containsKey("total_fee")){
            this.total_fee= Convert.toInt(mi_w_map.get("total_fee"),0) ;
        }
        if(mi_w_map.containsKey("settlement_total_fee")){
            this.settlement_total_fee= Convert.toInt(mi_w_map.get("settlement_total_fee"),0) ;
        }
        if(mi_w_map.containsKey("refund_fee")){
            this.refund_fee= Convert.toInt(mi_w_map.get("refund_fee"),0) ;
        }
        if(mi_w_map.containsKey("settlement_refund_fee")){
            this.settlement_refund_fee= Convert.toInt(mi_w_map.get("settlement_refund_fee"),0) ;
        }

        if(mi_w_map.containsKey("refund_status")){
            this.refund_status=mi_w_map.get("refund_status");
        }
        if(mi_w_map.containsKey("success_time")){
            this.success_time=mi_w_map.get("success_time");
        }

        if(mi_w_map.containsKey("refund_recv_accout")){
            this.refund_recv_accout=mi_w_map.get("refund_recv_accout");
        }

        if(mi_w_map.containsKey("refund_account")){
            this.refund_account=mi_w_map.get("refund_account");
        }

        if(mi_w_map.containsKey("refund_request_source")){
            this.refund_request_source=mi_w_map.get("refund_request_source");
        }



    }


    /**
     * 业务结果 返回状态码
     */
    private String return_code;

    /**
     * 返回信息
     */
    private String return_msg;

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mch_id;

    /**
     * 随机字符串
     */
    private String nonce_str;

    /**
     * 随机字符串
     */
    private String req_info;


    /**
     * 微信订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 商户退款单号
     */
    private String out_refund_no;

    /**
     * 微信退款单号
     */
    private String refund_id;

    /**
     * 标价金额
     */
    private Integer total_fee;

    /**
     * 应结订单金额
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
     */
    private Integer settlement_total_fee;

    /**
     * 退款金额
     */
    private Integer refund_fee;

    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Integer settlement_refund_fee;


    /**
     * 退款状态
     *
     * SUCCESS-退款成功
     * CHANGE-退款异常
     * REFUNDCLOSE—退款关闭
     */
    private String refund_status;


    /**
     * 退款成功时间
     * 资金退款至用户帐号的时间，格式2017-12-15 09:46:01
     */
    private String success_time;

    /**
     * 退款入账账户
     *
     *
     * 取当前退款单的退款入账方
     *
     * 1）退回银行卡：
     * {银行名称}{卡类型}{卡尾号}
     *
     * 2）退回支付用户零钱:
     * 支付用户零钱
     *
     * 3）退还商户:
     * 商户基本账户
     * 商户结算银行账户
     *
     * 4）退回支付用户零钱通:
     * 支付用户零钱通
     */
    private String refund_recv_accout;


    /**
     * 退款资金来源
     *
     * REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
     * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
     */
    private String refund_account;


    /**
     * 退款发起来源
     *
     * API接口
     * VENDOR_PLATFORM商户平台
     */
    private String refund_request_source;
}