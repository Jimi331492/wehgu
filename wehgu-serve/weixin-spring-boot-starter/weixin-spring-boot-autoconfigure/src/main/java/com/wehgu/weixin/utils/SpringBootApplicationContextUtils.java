package com.wehgu.weixin.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBootApplicationContextUtils implements ApplicationContextAware {


    private static ApplicationContext applicationContext_currentclass;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext_currentclass=applicationContext;
    }


    public static  <T> T getBean(Class<T> requiredType){
        return applicationContext_currentclass.getBean(requiredType);
    }

    public static Object getBean(String name){
        return  applicationContext_currentclass.getBean(name);
    }


    public static String resolvePlaceholders(String valuekey){
        String res_str = applicationContext_currentclass.getEnvironment().resolvePlaceholders(valuekey);
        return res_str;
    }

    public static String getProperty(String key){
        String property = applicationContext_currentclass.getEnvironment().getProperty(key);
        return property;
    }
}