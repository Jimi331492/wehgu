package com.wehgu.weixin.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.jfinal.weixin.sdk.api.*;

import com.wehgu.weixin.common.exception.WebServerInnerException;
import com.wehgu.weixin.config.WeiXinProperties;
import com.wehgu.weixin.entity.WxMenu;
import com.wehgu.weixin.entity.WxMenuInfo;
import com.wehgu.weixin.entity.WxMenuRoot;
import com.wehgu.weixin.entity.WxMenuSub;
import com.wehgu.weixin.utils.WxExtUtilsWithSpring;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/wx_menu")
public class WxMenuController extends WxApiController {


    @Resource
    private WeiXinProperties weiXinProperties;

    @Resource
    private WxExtUtilsWithSpring wxExtUtilsWithSpring;


    /**
     * 获取当前的微信公众号菜单信息
     * @return 操作的结果
     */
    @RequestMapping(path = "/get_menu")
    public String getMenu(){
        ApiResult result = weixin.call(MenuApi::getMenu);
        return result.getJson();
    }


    /**
     * 删除微信公众号菜单
     * @return 操作的结果
     */
    @RequestMapping(path = "/delete")
    public String deleteMenu(){
        ApiResult apiResult = weixin.call(MenuApi::deleteMenu);
        return JSONUtil.toJsonStr(apiResult);
    }

    /**
     * 重置微信公众号菜单
     * @return 操作的结果
     */
    @RequestMapping(path = "/reset")
    public String resetMenu(){
        List<WxMenuInfo> wxMenuInfos = weiXinProperties.getWxMenuInfos();
        if(wxMenuInfos==null||wxMenuInfos.size()==0){
            return "没有配置可以重置的菜单";
        }
        ApiConfig apiConfig;
        try{
            if(StrUtil.isBlank(appid)){
                apiConfig = ApiConfigKit.getApiConfig();
            }else {
                apiConfig=ApiConfigKit.getApiConfig(appid);
            }
        }catch (Exception ex){
            throw new WebServerInnerException("没有配置默认的公众号");
        }
        String finalAppId = apiConfig.getAppId();
        Optional<WxMenuInfo> wxMenuInfoOptional = wxMenuInfos.stream().filter(x -> StrUtil.isNotBlank(x.getWx_GZH_appId()) && x.getWx_GZH_appId().equals(finalAppId)).findFirst();
        String deal_res=null;
        if(wxMenuInfoOptional.isPresent()){
            WxMenuInfo wxMenuInfo = wxMenuInfoOptional.get();
            WxMenuRoot wxMenuRoot = wxMenuInfo.getWxMenuRoot();
            if(wxMenuRoot!=null){
                String prefix_url=wxExtUtilsWithSpring.getWXMenuPrefix();
                deal_res = dealWxMenu(wxMenuRoot, prefix_url, appid);
            }
        }
        String finalDeal_res = deal_res;
        ApiResult apiResult =weixin.call(()->MenuApi.createMenu(finalDeal_res));
        return JSONUtil.toJsonStr(apiResult);
    }

    /**
     * 传递微信菜单相关信息,进行菜单的相关设置 路径是项目里的相对路径
     * @param wxMenuInfo 微信菜单信息
     * @return 执行的结果
     */
    @RequestMapping(path = "/create_auto",method = RequestMethod.POST)
    public String createMenuAuto(@RequestBody WxMenuInfo wxMenuInfo){
        String deal_res=null;
        WxMenuRoot wxMenuRoot = wxMenuInfo.getWxMenuRoot();
        if(wxMenuRoot!=null){
            String prefix_url=wxExtUtilsWithSpring.getWXMenuPrefix();
            WxMenuRoot wxMenuRoot1 = ObjectUtil.cloneByStream(wxMenuRoot);
            deal_res = dealWxMenu(wxMenuRoot1, prefix_url, appid);
        }
        String finalDeal_res = deal_res;
        ApiResult apiResult =weixin.call(()->MenuApi.createMenu(finalDeal_res));
        return JSONUtil.toJsonStr(apiResult);
    }

    /**
     * 创建微信菜单 以原生的json字符串为参数 不做任何处理
     * @param jsonstr 菜单信息的json字符串
     * @return 创建菜单的返回结果
     */
    @RequestMapping(path = "/create_origin",method = RequestMethod.POST)
    public String create_origin(@RequestParam(name = "json_str")String jsonstr){
        ApiResult apiResult =weixin.call(()->MenuApi.createMenu(jsonstr));
        return JSONUtil.toJsonStr(apiResult);
    }





    private String dealWxMenu(WxMenuRoot weiXinRoot,String prefix_url,String appId){
        String visit_prefix = prefix_url;
        List<WxMenu> list_weixinmenu = weiXinRoot != null ? weiXinRoot.getButton() : null;
        if (list_weixinmenu != null && list_weixinmenu.size() > 0) {
            for (WxMenu wxMenu : list_weixinmenu) {
                if (wxMenu == null) {
                    continue;
                }
                if (!StrUtil.isBlank(wxMenu.getUrl())&&wxMenu.getIsUrlToAuthrize()) {
                    String tempurl = SnsAccessTokenApi.getAuthorizeURL(appId,visit_prefix + wxMenu.getUrl(),false);
                    wxMenu.setUrl(tempurl);
                }

                List<WxMenuSub> list_weixinsubbutton = wxMenu.getSub_button();
                if (list_weixinsubbutton != null) {
                    for (WxMenuSub wxMenuSub : list_weixinsubbutton) {

                        if (!StrUtil.isBlank(wxMenuSub.getUrl())&&wxMenuSub.getIsUrlToAuthrize()) {
                            String tempurl = SnsAccessTokenApi.getAuthorizeURL(appId,visit_prefix + wxMenuSub.getUrl(),false);
                            wxMenuSub.setUrl(tempurl);
                        }
                    }
                }
            }
        }
        String menuJsonStr="";
        if(weiXinRoot!=null){
            menuJsonStr=JSONUtil.toJsonStr(weiXinRoot);
        }
        return  menuJsonStr;
    }

}