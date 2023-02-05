package com.wehgu.weixin.service;

import cn.hutool.core.util.StrUtil;
import com.wehgu.weixin.common.exception.WebServerInnerException;
import com.wehgu.weixin.config.WeixinPayProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class ParterInfoGetService {

    @Resource
    private WeixinPayProperties weixinPayProperties;


    public WeixinPayProperties.WeixinPaySecretInfo getCurrentParterInfoByAppIdAndMchId(String appid, String mch_id){
        WeixinPayProperties.WeixinPayInfo currentPayInfo = getCurrentPayInfo(appid);
        if (currentPayInfo == null) {
            throw new WebServerInnerException("支付信息配置无效");
        }
        WeixinPayProperties.WeixinPaySecretInfo currentParterInfo = getCurrentParterInfo(currentPayInfo, mch_id);
        if (currentParterInfo == null) {
            throw new WebServerInnerException("商户信息配置无效");
        }
        String paternerKey = currentParterInfo.getPaterner_key();
        if (StrUtil.isBlank(paternerKey)) {
            throw new WebServerInnerException("商户密钥信息配置无效");
        }
        String cert_path = currentParterInfo.getCert_path();
        if (StrUtil.isBlank(cert_path)) {
            throw new WebServerInnerException("商户证书配置无效");
        }
        return currentParterInfo;
    }



    public String getCurrentParterKeyByAppIdAndMchId(String appid,String mch_id){
        WeixinPayProperties.WeixinPayInfo currentPayInfo = getCurrentPayInfo(appid);
        if (currentPayInfo == null) {
            throw new WebServerInnerException("支付信息配置无效");
        }
        WeixinPayProperties.WeixinPaySecretInfo currentParterInfo = getCurrentParterInfo(currentPayInfo, mch_id);
        if (currentParterInfo == null) {
            throw new WebServerInnerException("商户信息配置无效");
        }
        String paternerKey = currentParterInfo.getPaterner_key();
        if (StrUtil.isBlank(paternerKey)) {
            throw new WebServerInnerException("商户密钥信息配置无效");
        }
        return paternerKey;
    }


    public String getCurrentParterCertpathByAppIdAndMchId(String appid,String mch_id){
        WeixinPayProperties.WeixinPayInfo currentPayInfo = getCurrentPayInfo(appid);
        if (currentPayInfo == null) {
            throw new WebServerInnerException("支付信息配置无效");
        }
        WeixinPayProperties.WeixinPaySecretInfo currentParterInfo = getCurrentParterInfo(currentPayInfo, mch_id);
        if (currentParterInfo == null) {
            throw new WebServerInnerException("商户信息配置无效");
        }
        String cert_path = currentParterInfo.getCert_path();
        if (StrUtil.isBlank(cert_path)) {
            throw new WebServerInnerException("商户证书配置无效");
        }
        return cert_path;
    }



    /**
     * 获取支付信息
     *
     * @param appid
     * @return
     */
    private WeixinPayProperties.WeixinPayInfo getCurrentPayInfo(String appid) {
        WeixinPayProperties.WeixinPayInfo current_pay_info = null;
        Optional<WeixinPayProperties.WeixinPayInfo> optionalWeixinPayInfo = weixinPayProperties.getWeixinPayInfos().stream().filter(x -> x.getAppId().equals(appid)).limit(1).findAny();
        if (optionalWeixinPayInfo.isPresent()) {
            current_pay_info = optionalWeixinPayInfo.get();
        }
        return current_pay_info;
    }

    /**
     * 获取商户信息和密钥信息
     *
     * @param current_pay_info
     * @param mch_id
     * @return
     */
    private WeixinPayProperties.WeixinPaySecretInfo getCurrentParterInfo(WeixinPayProperties.WeixinPayInfo current_pay_info, String mch_id) {
        WeixinPayProperties.WeixinPaySecretInfo current_parterner_info = null;
        Optional<WeixinPayProperties.WeixinPaySecretInfo> weixinPaySecretInfoOptional = current_pay_info.getWeixinPaySecretInfos().stream().filter((x) -> x.getMch_id().equals(mch_id)).limit(1).findAny();
        if (weixinPaySecretInfoOptional.isPresent()) {
            current_parterner_info = weixinPaySecretInfoOptional.get();
        }
        return current_parterner_info;
    }
}
