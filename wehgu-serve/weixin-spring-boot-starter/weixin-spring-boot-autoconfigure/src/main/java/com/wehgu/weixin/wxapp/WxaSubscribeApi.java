package com.wehgu.weixin.wxapp;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.wxaapp.api.WxaAccessTokenApi;

public class WxaSubscribeApi {

    private static final String sendApiUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    public static ApiResult send(String jsonStr) {
        String jsonResult = HttpUtils.post(sendApiUrl + WxaAccessTokenApi.getAccessTokenStr(), jsonStr);
        return new ApiResult(jsonResult);
    }

    public static ApiResult send(WxaSubscribeMsg subscribeMsg) {
        return send(subscribeMsg.build());
    }


}