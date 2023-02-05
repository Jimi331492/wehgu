package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AdvancePayResInfo {


    public AdvancePayResInfo(){}

    public AdvancePayResInfo(Map<String,String> params){
        if(params.containsKey("return_code")){
            this.return_code=params.get("return_code");
        }
        if(params.containsKey("return_msg")){
            this.return_msg=params.get("return_msg");
        }

        if(params.containsKey("appid")){
            this.appid=params.get("appid");
        }
        if(params.containsKey("mch_id")){
            this.mch_id=params.get("mch_id");
        }
        if(params.containsKey("device_info")){
            this.device_info=params.get("device_info");
        }
        if(params.containsKey("nonce_str")){
            this.nonce_str=params.get("nonce_str");
        }
        if(params.containsKey("sign")){
            this.sign=params.get("sign");
        }
        if(params.containsKey("sign_type")){
            this.sign_type=params.get("sign_type");
        }
        if(params.containsKey("result_code")){
            this.result_code=params.get("result_code");
        }

        if(params.containsKey("err_code")){
            this.err_code=params.get("err_code");
        }

        if(params.containsKey("err_code_des")){
            this.err_code_des=params.get("err_code_des");
        }

        if(params.containsKey("openid")){
            this.openid=params.get("openid");
        }
        if(params.containsKey("is_subscribe")){
            this.is_subscribe=params.get("is_subscribe");
        }

        if(params.containsKey("trade_type")){
            this.trade_type=params.get("trade_type");
        }

        if(params.containsKey("bank_type")){
            this.bank_type=params.get("bank_type");
        }

        if(params.containsKey("total_fee")){
            this.total_fee= Convert.toInt(params.get("total_fee"),0) ;
        }

        if(params.containsKey("settlement_total_fee")){
            this.settlement_total_fee= Convert.toInt(params.get("settlement_total_fee"),0) ;
        }

        if(params.containsKey("fee_type")){
            this.fee_type=params.get("fee_type");
        }
        if(params.containsKey("cash_fee")){
            this.cash_fee= Convert.toInt(params.get("cash_fee"),0) ;
        }

        if(params.containsKey("cash_fee_type")){
            this.cash_fee_type=params.get("cash_fee_type");
        }

        if(params.containsKey("coupon_fee")){
            this.coupon_fee= Convert.toInt(params.get("coupon_fee"),0) ;
        }
        if(params.containsKey("coupon_count")){
            this.coupon_count= Convert.toInt(params.get("coupon_count"),0) ;
        }

        if(params.containsKey("transaction_id")){
            this.transaction_id=params.get("transaction_id");
        }
        if(params.containsKey("out_trade_no")){
            this.out_trade_no=params.get("out_trade_no");
        }

        if(params.containsKey("attach")){
            this.attach=params.get("attach");
        }
        if(params.containsKey("time_end")){
            this.time_end=params.get("time_end");
        }

        this.setCollections(params);
    }


    /**
     * 返回状态码
     */
    private String return_code;

    /**
     * 返回信息
     */
    private String return_msg;

    /**
     * 微信公众号appid或小程序appid
     */
    private String appid;

    /**
     * 商户号
     */
    private String mch_id;

    /**
     * 设备号
     * 微信支付分配的终端设备号
     */
    private String device_info;

    /**
     * 随机字符串
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String sign_type;

    /**
     * 业务结果
     * 支付结果状态码
     */
    private String result_code;

    /**
     * 错误代码
     */
    private String err_code;

    /**
     * 错误代码描述
     */
    private String err_code_des;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 是否关注公众账号
     */
    private String is_subscribe;

    /**
     * 交易类型
     */
    private String trade_type;

    /**
     * 付款银行
     */
    private String bank_type;

    /**
     * 订单金额
     * 支付金额 单位:分
     */
    private Integer total_fee;

    /**
     * 应结订单金额
     */
    private Integer settlement_total_fee;

    /**
     * 货币种类
     */
    private String fee_type;

    /**
     * 现金支付金额
     */
    private Integer cash_fee;

    /**
     * 现金支付货币类型
     */
    private String cash_fee_type;

    /**
     * 总代金券金额
     */
    private Integer coupon_fee;

    /**
     * 代金券使用数量
     */
    private Integer coupon_count;

    /**
     * 代金券类型集合
     */
    private Map<Integer,String> coupon_types=new HashMap<>();

    /**
     * 代金券id集合
     */
    private Map<Integer,String> coupon_ids=new HashMap<>();

    /**
     * 单个代金券支付金额集合
     */
    private Map<Integer,Integer> coupon_fees=new HashMap<>();

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;


    /**
     * 商家数据包,原样返回
     */
    private String attach;

    /**
     * 支付完成时间 格式为yyyyMMddHHmmss
     */
    private String time_end;

    /**
     * 是否更新成功
     */
    private boolean is_pay_success;


    private void setCollections(Map<String,String> params){
        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_type_")).forEach((x)-> coupon_types.put(Convert.toInt( x.getKey().replace("coupon_type_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_fee_")).forEach((x)-> coupon_fees.put(Convert.toInt( x.getKey().replace("coupon_fee_","")),Convert.toInt( x.getValue())));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_id_")).forEach((x)-> coupon_ids.put(Convert.toInt( x.getKey().replace("coupon_id_","")), x.getValue()));
    }
}