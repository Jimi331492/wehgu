package com.wehgu.admin.utils;


import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class EmptyUtil {

    /**
     * @param bool
     * @param message
     * 判断为false抛出运行时异常并提示message
     */
    public static void bool(Boolean bool, String message) {
        if (!bool) {
            throw new RuntimeException(message);
        }
    }

    /**
     * @param obj
     * @param message
     * 判断各类obj为空抛出运行时异常并提示message
     * Long型 ==null||<0
     * List size<1||或者
     */
    public static <T> void isEmpty(T obj, String message) {

        if (obj instanceof Long) {
            if ((Long) obj < 0) {
                throw new RuntimeException(message);
            }
        }

        if (obj instanceof List) {
            if (((List) obj).isEmpty() || ((List) obj).size() < 1) {
                throw new RuntimeException(message);
            }
        }

        if (obj instanceof String) {
            if (StringUtils.isBlank((String) obj)) {
                throw new RuntimeException(message);
            }
        }

        if (obj == null) {
            throw new RuntimeException(message);
        }
    }
}
