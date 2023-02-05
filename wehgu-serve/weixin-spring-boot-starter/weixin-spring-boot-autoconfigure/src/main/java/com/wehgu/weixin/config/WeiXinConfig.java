package com.wehgu.weixin.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jfinal.config.*;
import com.jfinal.log.Slf4jLogFactory;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.json.Json;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.wehgu.weixin.controller.*;
import com.wehgu.weixin.utils.JFinalControllerFactory;

import java.util.List;

public class WeiXinConfig extends JFinalConfig {

    private WeiXinProperties weiXinProperties;

    public WeiXinProperties getWeiXinProperties() {
        return weiXinProperties;
    }

    public void setWeiXinProperties(WeiXinProperties weiXinProperties) {
        this.weiXinProperties = weiXinProperties;
    }

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(weiXinProperties.getDevMode());
        ApiConfigKit.setDevMode(me.getDevMode());
        me.setJsonFactory(() -> {
            Json tempjson=new Json() {
                @Override
                public String toJson(Object object) {
                    return JSONUtil.toJsonStr(object);
                }

                @Override
                public <T> T parse(String jsonString, Class<T> type) {
                    return JSONUtil.toBean(jsonString,type);
                }
            };
            return tempjson;
        });

        /**
         * 考虑到 可能需要引用spring容器中的bean,继承并重写了ControllerFactory方法
         * 便于注入spring容器中的bean
         * 属性注入的实现,暂时没有思路
         */
        me.setControllerFactory(new JFinalControllerFactory());
        me.setLogFactory(new Slf4jLogFactory());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configRoute(Routes me) {
        me.setMappingSuperClass(true);
        me.add(String.format("/%s/api",weiXinProperties.getServlet_prefix()), WeixinApiController.class,"/api");
        me.add(String.format("/%s/pay",weiXinProperties.getServlet_prefix()), WeixinPayController.class);

        /**
         * 因为业务的后置性和不确定性,WeixinMsgController具体实现可能要在子项目中实现
         * 配置了加载其他实现的全类名名称,便于扩展
         */
        String wx_msg_controller_full_name = weiXinProperties.getWx_msg_controller_full_name();
        Class the_wx_msg_controller_class;
        if(StrUtil.isBlank(wx_msg_controller_full_name)){
            the_wx_msg_controller_class= WxMessageController.class;
        }else {
            try {
                the_wx_msg_controller_class=  Class.forName(wx_msg_controller_full_name,true, WeiXinConfig.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                the_wx_msg_controller_class= WxMessageController.class;
            }
        }
        me.add(String.format("/%s/msg",weiXinProperties.getServlet_prefix()), the_wx_msg_controller_class);


        String sp_msg_controller_full_name = weiXinProperties.getSp_msg_controller_full_name();
        Class the_sp_msg_controller_class;
        if(StrUtil.isBlank(sp_msg_controller_full_name)){
            the_sp_msg_controller_class= MpMessageController.class;
        }else {
            try {
                the_sp_msg_controller_class=Class.forName(sp_msg_controller_full_name,true,WeiXinConfig.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                the_sp_msg_controller_class= MpMessageController.class;
            }
        }
        me.add(String.format("/%s/spmsg",weiXinProperties.getServlet_prefix()), the_sp_msg_controller_class);

    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void onStart() {
        super.onStart();
        //公众号配置
        List<ApiConfig> apiConfigs = weiXinProperties.getApiConfigs();
        if(apiConfigs!=null&&apiConfigs.size()>0){
            apiConfigs.forEach(ApiConfigKit::putApiConfig);
        }
        //小程序配置
        List<WxaConfig> wxaConfigs = weiXinProperties.getWxaConfigs();
        if(wxaConfigs!=null&&wxaConfigs.size()>0){
            wxaConfigs.forEach(WxaConfigKit::putWxaConfig);
        }
    }
}