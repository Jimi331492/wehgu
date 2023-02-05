package com.wehgu.admin.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String YYYYMMddHHmmss() {
        return formatter(LocalDateTime.now(), "YYYY-MM-dd");
    }
    public static String YYYYMMddHHmmss(LocalDateTime localDateTime) {
        return formatter(localDateTime, "YYYY-MM-dd");
    }

    public static String YYYYMMddHH() {
        return formatter(LocalDateTime.now(), "YYYY-MM-dd");
    }
    public static String YYYYMMddHH(LocalDateTime localDateTime) {
        return formatter(localDateTime, "YYYY-MM-dd HH:mm:ss");
    }

    public static String formatter(LocalDateTime localDateTime,String formatter) {
        LocalDateTime ldt = localDateTime;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatter);
        return ldt.format(dtf);
    }

}