package com.wehgu.weixin.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;

import java.util.Map;

public class WxExtUtil {



    public static boolean ApiConfigKitContainsAppId(String appId){
        Object object_res = ReflectUtil.getFieldValue(ApiConfigKit.class, "CFG_MAP");
        if(object_res==null){
            return false;
        }
        Map<String, ApiConfig> tempmap=(Map<String, ApiConfig>) object_res;
        return tempmap.containsKey(appId);
    }

    public static boolean WxaConfigKitContainsAppId(String appId){
        Object object_res = ReflectUtil.getFieldValue(WxaConfigKit.class, "CFG_MAP");
        if(object_res==null){
            return false;
        }
        Map<String, WxaConfig> tempmap=(Map<String, WxaConfig>) object_res;
        return tempmap.containsKey(appId);
    }



    public static String getApiConfigKitDefaultAppId(){
        String default_appId="";
        Object object_res = ReflectUtil.getFieldValue(ApiConfigKit.class, "CFG_MAP");
        if(object_res==null){
            return default_appId;
        }
        Map<String, ApiConfig> tempmap=(Map<String, ApiConfig>) object_res;
        if(tempmap.containsKey("_default_cfg_key_")){
            ApiConfig default_cfg_config = tempmap.get("_default_cfg_key_");
            default_appId=  default_cfg_config.getAppId();
        }
        return default_appId;
    }


    public static String getWxaConfigKitDefaultAppId(){
        String default_appId="";
        Object object_res = ReflectUtil.getFieldValue(WxaConfigKit.class, "CFG_MAP");
        if(object_res==null){
            return default_appId;
        }
        Map<String, WxaConfig> tempmap=(Map<String, WxaConfig>) object_res;
        if(tempmap.containsKey("_default_cfg_key_")){
            WxaConfig default_cfg_config = tempmap.get("_default_cfg_key_");
            default_appId=  default_cfg_config.getAppId();
        }
        return default_appId;
    }



    public static String getOpenIdByCode(String appId,String code){
        if(StrUtil.isBlank(appId)){
            return null;
        }
        ApiConfig apiConfig = ApiConfigKit.getApiConfig(appId);
        SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(apiConfig.getAppId(), apiConfig.getAppSecret(), code);
        if(snsAccessToken!=null){
            return snsAccessToken.getOpenid();
        }
        return null;
    }

//    public static String getTelePhoneByCode(String appId,String code){
//        if(StrUtil.isBlank(appId)){
//            return null;
//        }
//        ApiConfig apiConfig = ApiConfigKit.getApiConfig(appId);
//        IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
//        if(snsAccessToken!=null){
//            return snsAccessToken.getOpenid();
//        }
//        return null;
//    }

    /**
     * 小程序自定义会话id
     * @param sessionId uuid字符串
     * @return 返回自定义会话id
     */
    public static String getSpSessionIdKey(String sessionId){
        if(StrUtil.isBlank(sessionId)){
            sessionId= UUID.fastUUID().toString(true);
        }
        return "wxa:session:" +
                sessionId;
    }
}