package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Data
public class SearchWxRefundResponse {

    public SearchWxRefundResponse(){

    }

    public SearchWxRefundResponse(Map<String,String> params){
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
        if(params.containsKey("total_refund_count")){
            this.total_refund_count= Convert.toInt(params.get("total_refund_count"));
        }
        if(params.containsKey("transaction_id")){
            this.transaction_id=params.get("transaction_id");
        }
        if(params.containsKey("out_trade_no")){
            this.out_trade_no=params.get("out_trade_no");
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
        if(params.containsKey("refund_count"))
        {
            this.refund_count=Convert.toInt(params.get("refund_count"),0);
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
     * 订单总退款次数
     *
     * 订单总共已发生的部分退款次数，当请求参数传入offset后有返回
     */
    private Integer total_refund_count;

    /**
     * 微信订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

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
     * 标价币种
     * 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String fee_type;

    /**
     * 现金支付金额
     */
    private Integer cash_fee;

    /**
     * 退款笔数
     * 当前返回退款笔数
     */
    private Integer refund_count;

    /**
     * 商户退款单号集合
     */
    private Map<Integer,String> out_refund_nos=new HashMap<>();

    /**
     * 微信退款单号集合
     */
    private Map<Integer,String> refund_ids=new HashMap<>();

    /**
     * 退款渠道集合
     *
     * ORIGINAL—原路退款
     * BALANCE—退回到余额
     * OTHER_BALANCE—原账户异常退到其他余额账户
     * OTHER_BANKCARD—原银行卡异常退到其他银行卡
     */
    private Map<Integer,String> refund_channels=new HashMap<>();

    /**
     * 申请退款金额集合
     * 退款总金额,单位为分,可以做部分退款
     */
    private Map<Integer,Integer> refund_fees=new HashMap<>();

    /**
     * 退款金额集合
     * 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Map<Integer,Integer> settlement_refund_fees=new HashMap<>();

    /**
     * 代金券类型集合
     */
    private Map<Integer,Map<Integer,String>> coupon_types=null;

    /**
     * 总代金券扣款金额集合
     */
    private Map<Integer,Integer> coupon_refund_fees=new HashMap<>();

    /**
     * 退款代金券使用数量集合
     */
    private Map<Integer,Integer> coupon_refund_counts=new HashMap<>();

    /**
     * 退款代金券ID集合
     */
    private Map<Integer,Map<Integer,String>> coupon_refund_ids=null;

    /**
     * 单个代金券退款金额集合
     */
    private Map<Integer,Map<Integer,Integer>> single_coupon_refund_fees=null;

    /**
     * 退款状态集合
     *
     * SUCCESS—退款成功
     * REFUNDCLOSE—退款关闭。
     * PROCESSING—退款处理中
     * CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款
     */
    private Map<Integer,String> refund_status=new HashMap<>();

    /**
     * 退款资金来源集合
     *
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款/基本账户
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款
     */
    private Map<Integer,String> refund_accounts=new HashMap<>();

    /**
     * 退款入账账户集合
     *
     *
     * 取当前退款单的退款入账方
     * 1）退回银行卡：
     * {银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:
     * 支付用户零钱
     * 3）退还商户:
     * 商户基本账户
     * 商户结算银行账户
     * 4）退回支付用户零钱通:
     * 支付用户零钱通
     */
    private Map<Integer,String> refund_recv_accouts=new HashMap<>();

    /**
     * 退款成功时间，当退款状态为退款成功时有返回
     */
    private Map<Integer,String> refund_success_times=new HashMap<>();



    private void setCollections(Map<String,String> params){
        params.entrySet().stream().filter(x -> x.getKey().contains("out_refund_no_")).forEach((x)-> out_refund_nos.put(Convert.toInt( x.getKey().replace("out_refund_no_","")), x.getValue()));
        params.entrySet().stream().filter(x -> x.getKey().contains("refund_id_")).forEach((x)-> refund_ids.put(Convert.toInt( x.getKey().replace("refund_id_","")), x.getValue()));
        params.entrySet().stream().filter(x -> x.getKey().contains("refund_channel_")).forEach((x)-> refund_channels.put(Convert.toInt( x.getKey().replace("refund_channel_","")), x.getValue()));
        params.entrySet().stream().filter(x -> x.getKey().contains("refund_fee_")).forEach((x)-> refund_fees.put(Convert.toInt( x.getKey().replace("refund_fee_","")),Convert.toInt(x.getValue())));

        params.entrySet().stream().filter(x -> x.getKey().contains("settlement_refund_fee_")).forEach((x)->{
            String currentkey = x.getKey().replace("settlement_refund_fee_", "");
            if(!currentkey.contains("_")){
                settlement_refund_fees.put(Convert.toInt(x.getKey().replace("settlement_refund_fee_","")),Convert.toInt(x.getValue()));
            }
        });

        coupon_types= params.entrySet().stream().filter(x -> x.getKey().contains("coupon_type_")).collect(
                (Supplier<HashMap<Integer, Map<Integer, String>>>) HashMap::new,
                (x, y) -> {
                    String[] split = y.getKey().replace("coupon_type_", "").split("_");
                    Integer indexkey_main = Convert.toInt(split[0]);
                    Integer indexkey_child=Convert.toInt(split[1]);
                    if(!x.containsKey(indexkey_main)){
                        x.put(indexkey_main,new HashMap<>());
                    }
                    x.get(indexkey_main).put(indexkey_child,y.getValue());
                },
                HashMap::putAll);

        params.entrySet().stream().filter(x -> x.getKey().contains("coupon_refund_count_")).forEach((x)-> coupon_refund_counts.put(Convert.toInt( x.getKey().replace("coupon_refund_count_","")),Convert.toInt(x.getValue())));

        coupon_refund_ids= params.entrySet().stream().filter(x -> x.getKey().contains("coupon_refund_id_")).collect(
                (Supplier<HashMap<Integer, Map<Integer, String>>>) HashMap::new,
                (x, y) -> {
                    String[] split = y.getKey().replace("coupon_refund_id_", "").split("_");
                    Integer indexkey_main = Convert.toInt(split[0]);
                    Integer indexkey_child=Convert.toInt(split[1]);
                    if(!x.containsKey(indexkey_main)){
                        x.put(indexkey_main,new HashMap<>());
                    }
                    x.get(indexkey_main).put(indexkey_child,y.getValue());
                },
                HashMap::putAll);

        single_coupon_refund_fees= params.entrySet().stream().filter(x -> x.getKey().contains("coupon_refund_fee_")).collect(
                (Supplier<HashMap<Integer, Map<Integer, Integer>>>) HashMap::new,
                (x, y) -> {
                    String current_key = y.getKey().replace("coupon_refund_fee_", "");
                    if(current_key.contains("_")){
                        String[] split = y.getKey().replace("coupon_refund_fee_", "").split("_");
                        Integer indexkey_main = Convert.toInt(split[0]);
                        Integer indexkey_child=Convert.toInt(split[1]);
                        if(!x.containsKey(indexkey_main)){
                            x.put(indexkey_main,new HashMap<>());
                        }
                        x.get(indexkey_main).put(indexkey_child,Convert.toInt(y.getValue()));
                    }
                },
                HashMap::putAll);

        params.entrySet().stream().filter(x -> x.getKey().contains("refund_status_")).forEach((x)-> refund_status.put(Convert.toInt( x.getKey().replace("refund_status_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("refund_account_")).forEach((x)-> refund_accounts.put(Convert.toInt( x.getKey().replace("refund_account_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("refund_recv_accout_")).forEach((x)-> refund_recv_accouts.put(Convert.toInt( x.getKey().replace("refund_recv_accout_","")), x.getValue()));

        params.entrySet().stream().filter(x -> x.getKey().contains("refund_success_time_")).forEach((x)-> refund_success_times.put(Convert.toInt( x.getKey().replace("refund_success_time_","")), x.getValue()));


    }
}