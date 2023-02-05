package com.wehgu.weixin.entity.wxpay;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SearchWxRefundRequest {

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

    /**
     * 商户退款单号
     */
    private String out_refund_no;

    /**
     * 微信退款单号
     */
    private String refund_id;

    /**
     * 偏移量
     *
     * 偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录
     */
    private Integer offset;

    public Map<String,String> toQueryMap(){
        Map<String,String> map=new HashMap<>();
        if(StrUtil.isNotBlank(this.transaction_id)){
            map.put("transaction_id",this.transaction_id);
        }
        if(StrUtil.isNotBlank(this.out_trade_no)){
            map.put("out_trade_no",this.out_trade_no);
        }
        if(StrUtil.isNotBlank(this.out_refund_no)){
            map.put("out_refund_no",this.out_refund_no);
        }
        if(StrUtil.isNotBlank(this.refund_id)){
            map.put("refund_id",this.refund_id);
        }
        if(this.offset!=null&&this.offset>0){
            map.put("offset",this.offset.toString());
        }
        return map;
    }
}