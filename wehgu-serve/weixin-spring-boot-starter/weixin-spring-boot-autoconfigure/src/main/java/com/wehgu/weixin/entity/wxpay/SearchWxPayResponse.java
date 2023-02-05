package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SearchWxPayResponse {


    public SearchWxPayResponse(){

    }

    public SearchWxPayResponse(Map<String,String> params){

        /**
         * 必定有的信息
         */
        if(params.containsKey("return_code")){
            this.return_code=params.get("return_code");
        }
        if(params.containsKey("return_msg")){
            this.return_msg=params.get("return_msg");
        }

        /**
         * return_code的值为SUCCESS时有的信息
         */

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

        if(params.containsKey("result_code")){
            this.result_code=params.get("result_code");
        }

        if(params.containsKey("err_code")){
            this.err_code=params.get("err_code");
        }

        if(params.containsKey("err_code_des")){
            this.err_code_des=params.get("err_code_des");
        }
        /**
         * 当return_code,result_code,trade_state的值都为SUCCESS的时候返回的信息
         *
         * 如果trade_state的值都不为SUCCESS,则返回out_trade_no和attach
         */
        if(params.containsKey("device_info")){
            this.device_info=params.get("device_info");
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
        if(params.containsKey("trade_state")){
            this.trade_state=params.get("trade_state");
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

        if(params.containsKey("trade_state_desc")){
            this.trade_state_desc=params.get("trade_state_desc");
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
     * 业务结果
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
     * 微信支付分配的终端设备号,选填
     */
    private String device_info;

    /**
     *用户在商户appid下的唯一标识
     */
    private String openid;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注
     */
    private String is_subscribe;

    /**
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
     */
    private String trade_type;

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（付款码支付）
     * USERPAYING--用户支付中（付款码支付）
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    private String trade_state;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识
     */
    private String bank_type;

    /**
     * 标价金额
     * 订单总金额，单位为分
     */
    private Integer total_fee;

    /**
     * 应结订单金额
     * 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
     */
    private Integer settlement_total_fee;

    /**
     * 标价币种
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String fee_type;

    /**
     * 现金支付金额
     * 现金支付金额订单现金支付金额
     */
    private Integer cash_fee;

    /**
     * 现金支付币种
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String cash_fee_type;

    /**
     * 代金券金额
     */
    private Integer coupon_fee;

    /**
     *代金券使用数量
     */
    private Integer coupon_count;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private String time_end;

    /**
     * 交易状态描述
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    private String trade_state_desc;

    /**
     * 代金券类型集合  索引和类型
     */
    private Map<Integer,String> coupon_types=new HashMap<>();

    /**
     * 单个代金券退款金额集合 索引和金额
     */
    private Map<Integer,Integer> coupon_fees=new HashMap<>();

    /**
     * 退款代金券ID集合 索引和代金券ID
     */
    private Map<Integer,String> coupon_ids=new HashMap<>();

    private void setCollections(Map<String,String> params){

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_type_")).forEach((x)-> coupon_types.put(Convert.toInt( x.getKey().replace("coupon_type_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_fee_")).forEach((x)-> coupon_fees.put(Convert.toInt( x.getKey().replace("coupon_fee_","")),Convert.toInt( x.getValue())));

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_id_")).forEach((x)-> coupon_ids.put(Convert.toInt( x.getKey().replace("coupon_id_","")), x.getValue()));

    }


}