package com.wehgu.weixin.controller;


import cn.hutool.core.util.StrUtil;


import com.wehgu.weixin.common.ResultTemplate;
import com.wehgu.weixin.common.exception.SendToWebClientException;
import com.wehgu.weixin.utils.WxExtUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/wx_common_api")
public class WxCommonApiController extends WxApiController{

    @RequestMapping(value = "get_openid_by_code", method = RequestMethod.GET)
    public ResultTemplate getOpenIdByCode(String code){
        if(StrUtil.isBlank(code)){
            throw new SendToWebClientException("code无效");
        }
        String openId = WxExtUtil.getOpenIdByCode(appid, code);
        HashMap<String, String> map = new HashMap<>();
        map.put("openId",openId);
        return ResultTemplate.ok(200,"获取成功",map);
    }
}