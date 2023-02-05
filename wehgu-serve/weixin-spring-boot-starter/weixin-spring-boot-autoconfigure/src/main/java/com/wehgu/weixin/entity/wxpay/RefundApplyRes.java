package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RefundApplyRes {


    public RefundApplyRes(){

    }

    public RefundApplyRes(Map<String,String> params){
        if(params.containsKey("return_code")){
            this.return_code=params.get("return_code");
        }
        if(params.containsKey("return_msg")){
            this.return_msg=params.get("return_msg");
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
        if(params.containsKey("appid")){
            this.appid=params.get("appid");
        }
        if(params.containsKey("mch_id")){
            this.mch_id=params.get("mch_id");
        }
        if(params.containsKey("nonce_str")){
            this.nonce_str=params.get("nonce_str");
        }
        if(params.containsKey("sign")){
            this.sign=params.get("sign");
        }
        if(params.containsKey("transaction_id")){
            this.transaction_id=params.get("transaction_id");
        }
        if(params.containsKey("out_trade_no")){
            this.out_trade_no=params.get("out_trade_no");
        }
        if(params.containsKey("out_refund_no")){
            this.out_refund_no=params.get("out_refund_no");
        }
        if(params.containsKey("refund_id")){
            this.refund_id=params.get("refund_id");
        }
        if(params.containsKey("refund_fee")){
            this.refund_fee= Convert.toInt(params.get("refund_fee"),0) ;
        }
        if(params.containsKey("total_fee")){
            this.total_fee= Convert.toInt(params.get("total_fee"),0) ;
        }
        if(params.containsKey("cash_fee")){
            this.cash_fee= Convert.toInt(params.get("cash_fee"),0) ;
        }
        if(params.containsKey("settlement_refund_fee")){
            this.settlement_refund_fee= Convert.toInt(params.get("settlement_refund_fee"),0) ;
        }
        if(params.containsKey("settlement_total_fee")){
            this.settlement_total_fee= Convert.toInt(params.get("settlement_total_fee"),0) ;
        }
        if(params.containsKey("cash_refund_fee")){
            this.cash_refund_fee= Convert.toInt(params.get("cash_refund_fee"),0) ;
        }
        if(params.containsKey("coupon_refund_fee")){
            this.coupon_refund_fee= Convert.toInt(params.get("coupon_refund_fee"),0) ;
        }
        if(params.containsKey("coupon_refund_count")){
            this.coupon_refund_count= Convert.toInt(params.get("coupon_refund_count"),0) ;
        }
        if(params.containsKey("fee_type")){
            this.fee_type=params.get("fee_type");
        }
        if(params.containsKey("cash_fee_type")){
            this.cash_fee_type=params.get("cash_fee_type");
        }
        this.setCollections(params);
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
     * 业务结果
     *
     * SUCCESS/FAIL
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询
     * FAIL 提交业务失败
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
     * 签名
     */
    private String sign;

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
     * 退款金额
     */
    private Integer refund_fee;

    /**
     * 标价金额
     */
    private Integer total_fee;

    /**
     * 现金支付金额
     */
    private Integer cash_fee;

    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Integer settlement_refund_fee;

    /**
     * 应结订单金额
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
     */
    private Integer settlement_total_fee;

    /**
     * 标价币种
     * 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String fee_type;

    /**
     * 现金支付币种
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String cash_fee_type;

    /**
     * 现金退款金额
     *
     */
    private Integer cash_refund_fee;

    /**
     * 代金券退款总金额
     *
     * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    private Integer coupon_refund_fee;


    /**
     * 退款代金券使用数量
     *
     *
     */
    private Integer coupon_refund_count;

    /**
     * 代金券类型集合  索引和类型
     */
    private Map<Integer,String> coupon_types=new HashMap<>();

    /**
     * 单个代金券退款金额集合 索引和金额
     */
    private Map<Integer,Integer> coupon_refund_fees=new HashMap<>();

    /**
     * 退款代金券ID集合 索引和代金券ID
     */
    private Map<Integer,String> coupon_refund_ids=new HashMap<>();


    private void setCollections(Map<String,String> params){

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_type_")).forEach((x)-> coupon_types.put(Convert.toInt( x.getKey().replace("coupon_type_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_refund_fee_")).forEach((x)-> coupon_refund_fees.put(Convert.toInt( x.getKey().replace("coupon_refund_fee_","")),Convert.toInt( x.getValue())));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_refund_id_")).forEach((x)-> coupon_refund_ids.put(Convert.toInt( x.getKey().replace("coupon_refund_id_","")), x.getValue()));

    }
}