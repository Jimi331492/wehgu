package com.wehgu.weixin.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "weixin.config.pay")
public class WeixinPayProperties {

    private List<WeixinPayInfo> weixinPayInfos;


    @Data
    public static class WeixinPayInfo{
        private String appId;
        private List<WeixinPaySecretInfo> weixinPaySecretInfos;
    }

    @Data
    public static class WeixinPaySecretInfo{
        private String mch_id;
        private String paterner_key;
        private String cert_path;

        public String getPhiscalPath(){
            String res="";
            if(StrUtil.isBlank(cert_path)){
                return res;
            }
            if(cert_path.indexOf("classpath:")==0){
                try {
                    File file = ResourceUtils.getFile(cert_path);
                    res= file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                res=cert_path;
            }
            return res;
        }
    }
}