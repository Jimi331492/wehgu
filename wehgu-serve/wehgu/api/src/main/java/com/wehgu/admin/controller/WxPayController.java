package com.wehgu.admin.controller;

import com.alibaba.fastjson.JSONObject;

import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.utils.IpUtil;
import com.wehgu.admin.utils.wxPay.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@Api(tags = "微信支付-接口")
@RestController
@RequestMapping("/wx")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(WxPayController.class);
    /**
     * 发起微信支付
     * @param payParams
     * @return
     */
    @PostMapping(value = "/wxPay")
    @ApiOperation("微信支付下单")
    public ResultTemplate wxPay(@RequestBody Map<String,String> payParams, HttpServletRequest request){
        try {
            String code = "";
            String money = "";
            String wareName = "";
            if(payParams != null && payParams.size() > 0){
                code = payParams.get("code");
                money = payParams.get("money");
                wareName = payParams.get("wareName");

            }
            // 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
            //请求参数
            String grant_type="authorization_code";
            String params = "appid=" + WxPayConfig.appid + "&secret=" + WxPayConfig.app_secret + "&js_code=" + code + "&grant_type=" + grant_type;
            //发送请求
            String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
            JSONObject json=JSONObject.parseObject(sr);
            String openid= (String) json.get("openid");
            //解析相应内容（转换成json对象）
            logger.info(sr);
            //微信里面的价格是以分为单位
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            //商户订单号
            String out_trade_no= new RandomUtil().getRandomCode(10);
            //商品名称
            String body = wareName;
            //获取客户端的ip地址
            String spbill_create_ip = IpUtil.getIpAddr(request);
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", out_trade_no);//商户订单号
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);//支付成功后的回调地址
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);//支付方式
            packageParams.put("openid", openid);
            // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String prestr = PayUtil.createLinkString(packageParams);
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                    + "<openid>" + openid + "</openid>"
                    + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + money + "</total_fee>"
                    + "<trade_type>" + WxPayConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            logger.info("调试模式_统一下单接口 请求XML数据：{}",xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);
            //业务逻辑
            /**此处添加自己的业务逻辑代码end**/



            logger.info("调试模式_统一下单接口 返回XML数据：{}",result);
            // 将解析结果存储在HashMap中
            Map map = PayUtil.doXMLParse(result);
            logger.info("doXMLParse {}",map);
            String return_code = (String) map.get("return_code");//返回状态码
            Map<String, Object> response = new HashMap<String, Object>();//返回给小程序端需要的参数
            if(return_code.equals("SUCCESS")){
                String prepay_id = String.valueOf(map.get("prepay_id"));//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + WxPayConfig.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();
                response.put("paySign", paySign);
            }
            response.put("appid", WxPayConfig.appid);
            return ResultTemplate.ok("微信支付成功",response);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信支付成功的回调函数
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/wxNotify")
    @ApiOperation("微信支付回调函数")
    public ResultTemplate wxNotify(HttpServletRequest request, HttpServletResponse response)throws Exception{
        logger.info("进入了回调》》》》》》》》》》》》》》》》》》》》》》》》");
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        logger.info("接收到的报文：{}",notityXml);
        Map map = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = PayUtil.sign(validStr, WxPayConfig.key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名

            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
                /**此处添加自己的业务逻辑代码start**/
                logger.info("开始=================");
                String out_trade_no=(String) map.get("out_trade_no");
                String transaction_id=(String) map.get("transaction_id");
                String total_fee=(String) map.get("total_fee");

                logger.info("结束=============================");
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[出现错误]]></return_msg>" + "</xml> ";
        }
        logger.info(resXml);
        logger.info("微信支付回调数据结束");
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
        Map maps = PayUtil.doXMLParse(resXml);
        String returnCodes = (String) maps.get("return_code");
        return ResultTemplate.ok("回调成功",returnCodes);
    }
}
