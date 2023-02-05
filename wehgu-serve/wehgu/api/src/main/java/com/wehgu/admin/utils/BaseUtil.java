package com.wehgu.admin.utils;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BaseUtil {
	   /**
     * 获得指定数目的UUID 
     * @param number int 需要获得的UUID数量 
     * @return String[] UUID数组 
     */
    public static String[] getUUID(int number){
        if(number < 1){
            return null;
        }
        String[] retArray = new String[number];
        for(int i=0;i<number;i++){
            retArray[i] = getUUID();
        }
        return retArray;
    }

    /**
     * 获得一个UUID 
     * @return String UUID 
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号 
        return uuid.replaceAll("-", "");
    }
    /**
     * Md5解密
     * @param inStr
     * @return
     */
    public final static String convertMD5(String inStr)
    {
        char[] a = inStr.toCharArray();
        for (int i=0;i<a.length;i++)
        {
            a[i]=(char)(a[i]^'t');
        }
            String s = new String(a);
            return s;
    }

    /**
     * Md5加密
     * @param str
     * @return
     */
    public final static String MD5(String str) {//项目中的
        try {
              MessageDigest md5 = MessageDigest.getInstance("MD5");
              md5.update(str.getBytes());
              byte b[] = md5.digest();

              StringBuffer sb = new StringBuffer("");
              for (int n = 0; n < b.length; n++) {
                int i = b[n];
                if (i < 0) i += 256;
                if (i < 16) sb.append("0");
                sb.append(Integer.toHexString(i));
              }
              return sb.toString();  //32位加密
            } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
              return null;
            }
    }
    /**
     * 获取properties文件属性
     * @param keyWord
     * @return
     */
    public static String getProperties(String keyWord){
        Properties prop = new Properties();
        String value = null;
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("local.properties");
            prop.load(inputStream);
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Boolean isLinux() {
        boolean res = false;
        String fiel_separator = System.getProperty("file.separator");
        if (fiel_separator.equalsIgnoreCase("\\")) {
            res = false;
        } else {
            res = true;
        }
        return res;
    }

    public static Boolean isIDE(){
        String thebase = System.getProperty("catalina.base");
        if(StringUtils.isNotBlank(thebase)){
            return thebase.contains(".IntelliJIdea");
        }else {
            return false;
        }
    }

    public static String getSpSessionIdKey(String sessionId){
        if(StringUtils.isBlank(sessionId)){
            sessionId=getUUID();
        }
        return "wxa:session:" +
                sessionId;
    }

    public static boolean isWxClient(HttpServletRequest request){
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if(StringUtils.isBlank(userAgent)){
            return false;
        }
        return userAgent.toLowerCase().indexOf("micromessenger")>-1;
    }

    public static boolean isPwdComplex(String newPwd) {
        int digit = 0;
        int lowerString = 0;
        int upperString = 0;
        int special = 0;
        Boolean flag = true;
        if (StringUtils.isNotEmpty(newPwd) && newPwd.length() > 0) {
            for (int i = 0; i < newPwd.length(); i++) {
                if (newPwd.charAt(i) >= '0' && newPwd.charAt(i) <= '9') {
                    digit = 1;
                } else if (newPwd.charAt(i) >= 'a' && newPwd.charAt(i) <= 'z') {
                    lowerString = 1;
                } else if (newPwd.charAt(i) >= 'A' && newPwd.charAt(i) <= 'Z') {
                    upperString = 1;
                } else if (newPwd.charAt(i) == '(' || newPwd.charAt(i) == ')' || newPwd.charAt(i) == '-' || newPwd.charAt(i) == '.' || newPwd.charAt(i) == ':') {
                    special = 1;
                } else {
                    flag = false; // 包含特殊字符，但是不被允许
                }
            }
            if (digit != 1 || lowerString + upperString == 0) { // 必须由字母数字组成
                throw new RuntimeException("必须由字母数字组成");
            } else if (digit == 1 && lowerString + upperString != 0 && flag == false && special != 1) { // 包含不被允许的特殊字符
                throw new RuntimeException("包含不被允许的特殊字符");
            } else {  // 验证通过
                return true;
            }
        } else {
            throw new RuntimeException("密码为空");
        }
    }
    public static String getIp(HttpServletRequest request){
        //代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        //如果没有代理，则获取真实ip
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    };

}