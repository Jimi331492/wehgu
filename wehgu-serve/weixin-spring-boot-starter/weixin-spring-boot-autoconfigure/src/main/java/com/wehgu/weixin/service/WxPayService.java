package com.wehgu.weixin.service;


import cn.hutool.core.util.StrUtil;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;

import com.wehgu.weixin.common.exception.SendToWebClientException;
import com.wehgu.weixin.common.exception.WebServerInnerException;
import com.wehgu.weixin.config.WeixinPayProperties;
import com.wehgu.weixin.entity.wxpay.*;
import com.wehgu.weixin.utils.WxExtUtilsWithSpring;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WxPayService {

    @Resource
    private WxExtUtilsWithSpring wxExtUtilsWithSpring;

    @Resource
    private ParterInfoGetService parterInfoGetService;

    /**
     * 预支付订单的添加
     *
     * @param advancePayInfo 要支付订单相关信息
     * @return 用户支付的json字符串
     */
    public PreyPayRes prey_pay(AdvancePayInfo advancePayInfo) {
        PreyPayRes preyPayRes=new PreyPayRes();
        String appid = advancePayInfo.getApp_id();
        if (StrUtil.isBlank(appid)) {
            throw new WebServerInnerException("微信公众号appid不能为空");
        }
        String partner = advancePayInfo.getMch_id();
        if (StrUtil.isBlank(partner)) {
            throw new WebServerInnerException("收款的商户号不能为空");
        }
        String ip = advancePayInfo.getIp();
        if (StrUtil.isBlank(ip)) {
            throw new WebServerInnerException("支付用户的ip不能为空");
        }
        String openId = advancePayInfo.getOpen_id();
        if (StrUtil.isBlank(openId)) {
            throw new WebServerInnerException("支付用户的公众号openid不能为空");
        }

        /**
         * 获取默认的或配置的通知地址
         */
        String default_pay_notify_url = wxExtUtilsWithSpring.getPayNotifyUrl();
        /**
         * 获取密钥
         */
        String paternerKey= parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(appid,advancePayInfo.getMch_id());


        /**
         *生成统一下单的主要信息
         */
        Map<String, String> params= advancePayInfo.generatePushOrderMap(appid,partner, PaymentApi.TradeType.JSAPI.name(),default_pay_notify_url);

        /**
         * 生成密钥,并填充到map集合中
         */
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);
        /**
         * 统一下单请求发送
         */
        String xmlResult = PaymentApi.pushOrder(params);

        /**
         * 解析返回的微信订单信息,判断统一下单请求是否成功
         */
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
            log.error(return_msg);
            throw new SendToWebClientException("预支付订单创建失败,请稍后再试");
        }
        /**
         * 解析生成 用户支付的请求字符串
         */
        String prepay_id = result.get("prepay_id");
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appId", appid);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = PaymentKit.createSign(packageParams, paternerKey);
        packageParams.put("paySign", packageSign);

        String jsonStr = JsonUtils.toJson(packageParams);


        preyPayRes.setAdvancePayInfo(advancePayInfo);
        preyPayRes.setWxClientPayJsonStr(jsonStr);

        /**
         * 返回生成的json字符串,微信公众号支付使用
         */
        return preyPayRes;
    }

    /**
     * 微信公众号通知结果处理
     *
     * @param httpServletRequest   通知的请求信息
     * @param wxPayUpdateDBService 实现更新数据库的接口
     */
    public void getPayResAndUpdateDb(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, IWxPayUpdateDBService wxPayUpdateDBService) {
        AdvancePayResInfo payResInfo = null;
        String xmlMsg = HttpKit.readData(httpServletRequest);
        log.info("支付结果通知,{}", xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

        if(params!=null){
            payResInfo=new AdvancePayResInfo(params);
        }

        String paternerKey=parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(payResInfo.getAppid(),payResInfo.getMch_id());

        if (PaymentKit.verifyNotify(params, paternerKey)) {
            if (("SUCCESS").equals(payResInfo.getResult_code())) {
                payResInfo.set_pay_success(true);

                Map<String, String> xml = new HashMap<String, String>();
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                String outXml = PaymentKit.toXml(xml);

                //更新订单信息
                System.out.println("更新订单信息");
                boolean updateres = wxPayUpdateDBService.updateDbInfo(payResInfo);
                if(updateres){
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setContentType("text/plain");
                    PrintWriter writer=null;
                    try {
                        writer = httpServletResponse.getWriter();
                        writer.write(outXml);
                    } catch (IOException e) {
                        log.error("给微信服务器发生信息发生异常",e);
                    }finally {
                        if(writer!=null){
                            writer.flush();
                            writer.close();
                        }
                    }
                }
            }
        }
    }

    /**
     * 主动查询微信支付结果的接口
     * @param searchWxPayRequest 相关查询信息的封装
     * @return SearchWxPayResponse实例
     */
    public SearchWxPayResponse search_wxpay_res(SearchWxPayRequest searchWxPayRequest) {
        SearchWxPayResponse searchWxPayResponse=null;
        if (StrUtil.isBlank(searchWxPayRequest.getAppid())) {
            throw new WebServerInnerException("appid不能为空");
        }
        if (StrUtil.isBlank(searchWxPayRequest.getMch_id())) {
            throw new WebServerInnerException("商户号id不能为空");
        }
        if (StrUtil.isBlank(searchWxPayRequest.getOut_trade_no()) && StrUtil.isBlank(searchWxPayRequest.getTransaction_id())) {
            throw new SendToWebClientException("微信订单号和商户订单号,必须有一个有效");
        }

        String paternerKey =parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(searchWxPayRequest.getAppid(),searchWxPayRequest.getMch_id());

        Map<String, String> search_res_map = null;

        if (StrUtil.isNotBlank(searchWxPayRequest.getTransaction_id())) {
            search_res_map = PaymentApi.queryByTransactionId(searchWxPayRequest.getAppid(), searchWxPayRequest.getMch_id(), paternerKey, searchWxPayRequest.getTransaction_id());
        } else if (StrUtil.isNotBlank(searchWxPayRequest.getOut_trade_no())) {
            search_res_map = PaymentApi.queryByOutTradeNo(searchWxPayRequest.getAppid(), searchWxPayRequest.getMch_id(), paternerKey, searchWxPayRequest.getOut_trade_no());
        }
        if(search_res_map!=null){
            searchWxPayResponse=new SearchWxPayResponse(search_res_map);
        }
        return searchWxPayResponse;
    }

    /**
     * 申请退款
     *
     * @param wxRefundInfo 申请退款的相关信息
     * @return RefundApplyRes实例
     */
    public RefundApplyRes refundApply(WxRefundInfo wxRefundInfo) {
        if (wxRefundInfo == null) {
            throw new WebServerInnerException("传入的参数为空");
        }
        String appid = wxRefundInfo.getAppid();
        if (StrUtil.isBlank(appid)) {
            throw new WebServerInnerException("微信公众号appid无效");
        }
        String mch_id = wxRefundInfo.getMch_id();
        if (StrUtil.isBlank(mch_id)) {
            throw new WebServerInnerException("商户号id无效");
        }

        WeixinPayProperties.WeixinPaySecretInfo currentParterInfo = parterInfoGetService.getCurrentParterInfoByAppIdAndMchId(appid,mch_id);
        String paternerKey = currentParterInfo.getPaterner_key();
        String cert_path = currentParterInfo.getCert_path();
        String cert_absolute_path=currentParterInfo.getPhiscalPath();
        if (StrUtil.isBlank(wxRefundInfo.getTransaction_id()) && StrUtil.isBlank(wxRefundInfo.getOut_trade_no())) {
            throw new SendToWebClientException("微信订单号和商户订单号必须有一个有效");
        }
        if (wxRefundInfo.getTotal_fee() == null || wxRefundInfo.getTotal_fee() == 0) {
            throw new SendToWebClientException("订单金额无效");
        }
        if (wxRefundInfo.getRefund_fee() == null || wxRefundInfo.getRefund_fee() == 0) {
            throw new SendToWebClientException("退款金额无效");
        }
        String default_refundNotifyUrl = wxExtUtilsWithSpring.getRefundNotifyUrl();
        Map<String, String> params = wxRefundInfo.generateWxRefundApplyMap(default_refundNotifyUrl);
        log.info("证书物理位置:{}",cert_absolute_path);
        Map<String, String> refund_res = PaymentApi.refund(params, paternerKey, cert_absolute_path);
        RefundApplyRes refundApplyRes = new RefundApplyRes(refund_res);
        return refundApplyRes;
    }


    public void refundNotify(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, IWxPayRefundUpdateDBService wxPayRefundUpdateDBService) {
        String xmlMsg = HttpKit.readData(httpServletRequest);
        log.info("退款结果通知,{}", xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
        if (params.get("return_code").equalsIgnoreCase("SUCCESS")) {
            String reqInfoSecret = params.get("req_info");
            String mch_id = params.get("mch_id");
            String appid = params.get("appid");

            String paternerKey=parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(appid,mch_id);
            log.info("要解密的内容是:");
            log.info(reqInfoSecret);
            String refundDecrypt = refundDecrypt(reqInfoSecret, paternerKey);
            log.info("解密后的结果是:");
            log.info(refundDecrypt);
            Map<String, String> reqInfoMap = PaymentKit.xmlToMap(refundDecrypt);
            RefundNotifyRes refundNotifyRes = new RefundNotifyRes(params, reqInfoMap);

            Map<String, String> xml = new HashMap<String, String>();
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            String outXml = PaymentKit.toXml(xml);
            if (wxPayRefundUpdateDBService != null) {
                boolean updateres = wxPayRefundUpdateDBService.updateDbInfo(refundNotifyRes);
                if(updateres){
                    ServletOutputStream outputStream=null;
                    try {
                        httpServletResponse.setContentType("text/plain");
                        outputStream = httpServletResponse.getOutputStream();
                        outputStream.write(outXml.getBytes());
                    } catch (IOException e) {
                        log.error("给微信服务器发生信息发生异常",e);
                    }finally {
                        if(outputStream!=null){
                            try {
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                log.error("outputstream释放发生异常",e);
                            }
                        }
                    }
                }
            }
        }
    }


    public SearchWxRefundResponse search_wx_refund_res(SearchWxRefundRequest searchWxRefundRequest) {
        SearchWxRefundResponse searchWxRefundResponse=null;
        if (searchWxRefundRequest == null) {
            throw new WebServerInnerException("传入的参数为空");
        }
        String appid = searchWxRefundRequest.getAppid();
        if (StrUtil.isBlank(appid)) {
            throw new WebServerInnerException("微信公众号appid无效");
        }
        String mch_id = searchWxRefundRequest.getMch_id();
        if (StrUtil.isBlank(mch_id)) {
            throw new WebServerInnerException("商户号id无效");
        }

        if (StrUtil.isBlank(searchWxRefundRequest.getOut_trade_no())
                && StrUtil.isBlank(searchWxRefundRequest.getTransaction_id())
                &&StrUtil.isBlank(searchWxRefundRequest.getRefund_id())
                &&StrUtil.isBlank(searchWxRefundRequest.getOut_refund_no())) {
            throw new WebServerInnerException("微信订单号,商户订单号,商户退款单号,微信退款单号,必须有一个有效");
        }

        String paternerKey=parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(searchWxRefundRequest.getAppid(),searchWxRefundRequest.getMch_id());

        Map<String,String> search_refund_res=null;
        if(StrUtil.isNotBlank(searchWxRefundRequest.getRefund_id())){
            search_refund_res=  PaymentApi.refundQueryByRefundId(searchWxRefundRequest.getAppid(),searchWxRefundRequest.getMch_id(),paternerKey,searchWxRefundRequest.getRefund_id());
        }else if(StrUtil.isNotBlank(searchWxRefundRequest.getOut_refund_no())){
            search_refund_res= PaymentApi.refundQueryByOutRefundNo(searchWxRefundRequest.getAppid(),searchWxRefundRequest.getMch_id(),paternerKey,searchWxRefundRequest.getOut_refund_no());
        }else if(StrUtil.isNotBlank(searchWxRefundRequest.getTransaction_id())){
            search_refund_res= PaymentApi.refundQueryByTransactionId(searchWxRefundRequest.getAppid(),searchWxRefundRequest.getMch_id(),paternerKey,searchWxRefundRequest.getTransaction_id());
        }else if(StrUtil.isNotBlank(searchWxRefundRequest.getOut_trade_no())){
            search_refund_res= PaymentApi.refundQueryByOutTradeNo(searchWxRefundRequest.getAppid(),searchWxRefundRequest.getMch_id(),paternerKey,searchWxRefundRequest.getOut_trade_no());
        }

        if(search_refund_res!=null){
            searchWxRefundResponse=new SearchWxRefundResponse(search_refund_res);
        }

        return searchWxRefundResponse;
    }

    public WxPayCloseOrderResponse closeOrder(WxPayCloseOrderRequest wxPayCloseOrderRequest){
        WxPayCloseOrderResponse wxPayCloseOrderResponse=null;
        if (wxPayCloseOrderRequest == null) {
            throw new WebServerInnerException("传入的参数为空");
        }
        String appid = wxPayCloseOrderRequest.getAppid();
        if (StrUtil.isBlank(appid)) {
            throw new WebServerInnerException("微信公众号appid无效");
        }
        String mch_id = wxPayCloseOrderRequest.getMch_id();
        if (StrUtil.isBlank(mch_id)) {
            throw new WebServerInnerException("商户号id无效");
        }

        if(StrUtil.isBlank(wxPayCloseOrderRequest.getOut_trade_no())){
            throw new WebServerInnerException("商户订单号不能为空");
        }

        String paternerKey=parterInfoGetService.getCurrentParterKeyByAppIdAndMchId(appid,mch_id);

        Map<String, String> order_close_res = PaymentApi.closeOrder(appid, mch_id, paternerKey, wxPayCloseOrderRequest.getOut_trade_no());

        if(order_close_res!=null){
            wxPayCloseOrderResponse=new WxPayCloseOrderResponse(order_close_res);
        }
        return wxPayCloseOrderResponse;
    }

    private String refundDecrypt(String reqInfoSecret, String mch_Id_Key) {
        String result = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] bytes = Base64.getDecoder().decode(reqInfoSecret);
            String md5key = DigestUtils.md5DigestAsHex(mch_Id_Key.getBytes()).toLowerCase();
            SecretKey secretKey = new SecretKeySpec(md5key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] resultbt = cipher.doFinal(bytes);
            result = new String(resultbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}