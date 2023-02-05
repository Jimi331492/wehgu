package com.wehgu.weixin.common;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;


    protected void  init(){

    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest req, HttpServletResponse res) {
        this.request=req;
        this.response=res;
        this.session = request.getSession();
        this.init();
    }
}