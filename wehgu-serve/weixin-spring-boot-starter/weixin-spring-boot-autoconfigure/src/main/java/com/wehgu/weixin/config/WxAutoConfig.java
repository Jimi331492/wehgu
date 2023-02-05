package com.wehgu.weixin.config;


import com.wehgu.weixin.controller.MpCommonApiController;
import com.wehgu.weixin.controller.WxCommonApiController;
import com.wehgu.weixin.controller.WxMenuController;
import com.wehgu.weixin.filter.JFinalWxFilter;
import com.wehgu.weixin.service.ParterInfoGetService;
import com.wehgu.weixin.service.TemplateMsgService;
import com.wehgu.weixin.service.WxPayService;
import com.wehgu.weixin.utils.WxExtUtilsWithSpring;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@EnableConfigurationProperties(value = {WeiXinProperties.class,WeixinPayProperties.class,WeiXinNetProperties.class})
@ConditionalOnWebApplication
@Configuration
public class WxAutoConfig {
    @Resource
    private WeiXinProperties weiXinProperties;

    @Resource
    private WeixinPayProperties weixinPayProperties;

    @Resource
    private WeiXinNetProperties weiXinNetProperties;

    @Bean
    public FilterRegistrationBean<JFinalWxFilter> jfinalFilter(){

        JFinalWxFilter jFinalWxFilter = new JFinalWxFilter(weiXinProperties);
        FilterRegistrationBean<JFinalWxFilter> filterRegistrationBean=new FilterRegistrationBean<>();
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.setFilter(jFinalWxFilter);
        filterRegistrationBean.setName("jfinal_wx_filter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public TemplateMsgService templateMsgService(){
        return new TemplateMsgService();
    }

    @Bean
    public WxPayService wxPayService(){
        return new WxPayService();
    }

    @Bean
    public ParterInfoGetService parterInfoGetService(){return new ParterInfoGetService();}

    @Bean
    public WxExtUtilsWithSpring wxExtUtilsWithSpring(){return new WxExtUtilsWithSpring();}

    @Bean
    public WxCommonApiController wxCommonApiController(){
        return new WxCommonApiController();
    }

    //控制器容器注入
    @Bean
    public WxMenuController wxMenuController(){
        return new WxMenuController();
    }

    @Bean
    public MpCommonApiController mpCommonApiController(){ return new MpCommonApiController();}
}