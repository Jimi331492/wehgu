package com.wehgu.weixin.filter;

import cn.hutool.core.util.StrUtil;

import com.jfinal.core.JFinalFilter;
import com.wehgu.weixin.config.WeiXinConfig;
import com.wehgu.weixin.config.WeiXinProperties;
import com.wehgu.weixin.service.IAppIdGet;
import com.wehgu.weixin.utils.SpringBootApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Slf4j
public class JFinalWxFilter implements Filter{



    public JFinalWxFilter(WeiXinProperties weiXinProperties){
        this.weiXinProperties=weiXinProperties;
        WeiXinConfig weiXinConfig = new WeiXinConfig();
        weiXinConfig.setWeiXinProperties(weiXinProperties);
        jFinalFilter=new JFinalFilter(weiXinConfig);
    }

    JFinalFilter jFinalFilter;


    private final WeiXinProperties weiXinProperties;

    public WeiXinProperties getWeiXinProperties() {
        return weiXinProperties;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        jFinalFilter.init(filterConfig);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletRequestWrapper httpServletRequestWrapper=new HttpServletRequestWrapper(httprequest){

            private final String param_name="appId";

            private IAppIdGet getCoreAppIdService(){
                IAppIdGet bean=null;
                try {
                    bean = SpringBootApplicationContextUtils.getBean(IAppIdGet.class);
                }catch (Exception ex){
                    log.error("配置com.wehgu.weixin.service.IAppIdGet为空或着该类的实现类有多个");
                }
                return  bean;
            }

            private String getAppId(){
                IAppIdGet coreAppIdService = getCoreAppIdService();
                if(coreAppIdService==null){
                    return null;
                }else {
                    return coreAppIdService.getAppId(httprequest);
                }
            }

            @Override
            public String getParameter(String name) {
                String valuetemp = super.getParameter(name);
                if(StrUtil.isBlank(valuetemp)){
                    if(param_name.equals(name)){
                        valuetemp= getAppId();
                    }
                }
                return valuetemp;
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                HashMap<String, String[]> newMap = new HashMap<>(super.getParameterMap());
                if(!newMap.containsKey(param_name)){
                    newMap.put(param_name,this.getParameterValues(param_name)) ;
                }
                return Collections.unmodifiableMap(newMap);
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] parameterValues = super.getParameterValues(name);
                if(parameterValues==null||parameterValues.length==0){
                    if(param_name.equals(name)){
                        String appId = getAppId();
                        if(StrUtil.isNotBlank(appId)){
                            parameterValues=new String[]{appId};
                        }
                    }
                }
                return parameterValues;
            }
        };


        String servletPath = httpServletRequestWrapper.getServletPath();

        if(StrUtil.isNotBlank(servletPath)){
            if(servletPath.startsWith(String.format("/%s/",weiXinProperties.getServlet_prefix()))){
                jFinalFilter.doFilter(httpServletRequestWrapper,response,chain);
            }else {
                chain.doFilter(httpServletRequestWrapper, response);
            }
        }
    }

    public void destroy() {
        jFinalFilter.destroy();
    }
}