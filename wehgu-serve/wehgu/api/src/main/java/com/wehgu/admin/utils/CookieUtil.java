package com.wehgu.admin.utils;

import com.wehgu.admin.config.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static Cookie generateLoginCookie(HttpServletRequest request, String token) {
        Cookie current_user_cookie=new Cookie(Constants.REQUEST_HEADER, token);
        current_user_cookie.setMaxAge(Integer.MAX_VALUE);
        current_user_cookie.setHttpOnly(true);
        current_user_cookie.setPath(request.getServletContext().getContextPath());
        return current_user_cookie;
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie current_user_cookie=new Cookie(Constants.REQUEST_HEADER, "null");
        current_user_cookie.setMaxAge(0);
        current_user_cookie.setHttpOnly(true);
        current_user_cookie.setPath(request.getServletContext().getContextPath());
        response.addCookie(current_user_cookie);
    }
}