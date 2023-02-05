package com.wehgu.weixin.entity.wxpay;

import lombok.Data;

import java.util.Map;

@Data
public class WxPayCloseOrderResponse {


    public WxPayCloseOrderResponse(){}

    public WxPayCloseOrderResponse(Map<String,String> params){
        if(params.containsKey("return_code")){
            this.return_code=params.get("return_code");
        }
        if(params.containsKey("return_msg")){
            this.return_msg=params.get("return_msg");
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
        if(params.containsKey("result_msg")){
            this.result_msg=params.get("result_msg");
        }
        if(params.containsKey("err_code")){
            this.err_code=params.get("err_code");
        }
        if(params.containsKey("err_code_des")){
            this.err_code_des=params.get("err_code_des");
        }
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
     *
     * SUCCESS/FAIL
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询
     * FAIL 提交业务失败
     */
    private String result_code;

    /**
     * 业务结果描述
     */
    private String result_msg;

    /**
     * 错误代码
     */
    private String err_code;

    /**
     * 错误代码描述
     */
    private String err_code_des;
}