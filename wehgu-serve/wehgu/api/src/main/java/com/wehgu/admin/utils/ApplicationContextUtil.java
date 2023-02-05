package com.wehgu.admin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * <p> 全局上下文工具类配置 </p>
 *
 * @description : 解决静态方法中mapper的调用
 */
@Slf4j
public class ApplicationContextUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        applicationContext = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
