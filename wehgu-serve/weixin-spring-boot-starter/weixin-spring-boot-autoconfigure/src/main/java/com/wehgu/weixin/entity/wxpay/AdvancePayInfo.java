package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.util.StrUtil;
import com.jfinal.weixin.sdk.kit.RandomKit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class AdvancePayInfo {

    /**
     * 微信公众号id
     */
    private String app_id;
    /**
     * 付款用户openid
     */
    private String open_id;
    /**
     * 收款商户号id
     */
    private String mch_id;

    /**
     * 商品详情
     */
    private String body;
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 支付金额,单位分
     */
    private Integer total_fee;

    /**
     * 下订单的用户ip
     */
    private String ip;

    /**
     * 支付结果通知地址
     */
    private String notify_url;

    /**
     * 交易起始时间
     * 订单生成时间，格式为yyyyMMddHHmmss
     */
    @Setter(value = AccessLevel.NONE)
    protected LocalDateTime time_start=LocalDateTime.now();

    /**
     * 交易结束时间
     * 订单失效时间，格式为yyyyMMddHHmmss
     */
    @Setter(value = AccessLevel.NONE)
    protected LocalDateTime time_expire;

    /**
     * 指定支付方式
     * 上传此参数no_credit--可限制用户不能使用信用卡支付
     *
     * 当为true时,Map传入no_credit
     */
    private boolean limit_pay=false;

    /**
     * 电子发票入口开放标识
     * Y，传入Y时，支付成功消息和支付详情页将出现开票入口
     *
     * 当为true时,Map传入Y
     */
    private boolean receipt=false;


    /**
     * 订单过期时间
     */
    private Integer order_expire_count;




    /**
     * 将必要的参数和当前对象整合成生成预支付订单信息
     * @param appid 公众号appid
     * @param partner 商户id
     * @param trade_type 交易类型
     * @param notify_url 默认通知地址
     * @return 返回主要的map集合,用于统一下单
     */
    public Map<String,String> generatePushOrderMap(String appid,String partner,String trade_type,String notify_url){
        Map<String, String> params = new HashMap<>();


        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        params.put("body", this.getBody());
        params.put("out_trade_no", this.getOut_trade_no());
        params.put("total_fee", this.getTotal_fee().toString());
        params.put("spbill_create_ip", this.getIp());
        params.put("nonce_str", RandomKit.genNonceStr());
        params.put("openid", this.getOpen_id());

        params.put("appid", appid);
        params.put("mch_id", partner);
        params.put("trade_type", trade_type);

        if(time_start!=null){
            params.put("time_start",time_start.format(dateTimeFormatter));
            if(order_expire_count!=null&&order_expire_count>0){
                time_expire= time_start.minusMinutes(order_expire_count*-1);
            }
        }
        if(time_expire!=null){
            params.put("time_expire",time_expire.format(dateTimeFormatter));
        }
        if(limit_pay){
            params.put("limit_pay","no_credit");
        }
        if(receipt){
            params.put("receipt","Y");
        }



        if(StrUtil.isBlank(this.getNotify_url())){
            if(StrUtil.isNotBlank(notify_url)){
                params.put("notify_url",notify_url);
            }
        }else {
            params.put("notify_url",this.getNotify_url());
        }
        return params;
    }

}