package com.wehgu.weixin.config;

import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.wxaapp.WxaConfig;
import com.wehgu.weixin.entity.WxMenuInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "weixin.config")
public class WeiXinProperties {

    /**
     * jfinal 引擎使用的servlet前缀
     */
    private String servlet_prefix="weixin";

    /**
     * 自定义微信消息处理控制器全类名
     */
    private String wx_msg_controller_full_name;

    /**
     * 自定义小程序消息处理控制器全类名
     */
    private String sp_msg_controller_full_name;

    /**
     * 是否开发模式
     */
    private Boolean devMode=false;

    /**
     * 公众号配置集合
     */
    private List<ApiConfig> apiConfigs=new ArrayList<>();

    /**
     * 小程序配置集合
     */
    private List<WxaConfig> wxaConfigs=new ArrayList<>();

    /**
     * 微信公众号菜单信息集合
     */
    private List<WxMenuInfo> wxMenuInfos=new ArrayList<>();
}