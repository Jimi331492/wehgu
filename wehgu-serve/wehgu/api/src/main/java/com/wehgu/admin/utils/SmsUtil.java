package com.wehgu.admin.utils;

import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.config.security.dto.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsUtil {

    public static boolean isValidCode(HttpServletRequest request, @RequestParam(name = "telephone") String telephone, @RequestParam(name = "code") String code){
        HttpSession session = request.getSession();
        //取出Session中手机号，验证码，存入的时间
        String sessionTelephone = (String) session.getAttribute("telephone");
        String sessionCode = (String) session.getAttribute("code");
        Long date = (Long) session.getAttribute("date");
        //若Session中手机号，验证码，存入的时间均为空
        if (StringUtils.isEmpty(sessionTelephone) && StringUtils.isEmpty(sessionCode)
                && session.getAttribute("date") == null) {
            return  false;
        }
        //设置Session中验证码的失效时间（这里为1分钟）
        if ((System.currentTimeMillis() - date) > 1000 * 60) {
            //超过5分钟清空Session，需重新发送验证码
            session.removeAttribute("telephone");
            session.removeAttribute("code");
            session.removeAttribute("date");
            return  false;
        }
        if (!sessionCode.equals(code)) {
            return  false;
        }

        return sessionTelephone.equals(telephone);
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            System.out.print("手机号应为11位数 ");
            return false;
        }else{
            String regPattern =  "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(phoneNumber);
            boolean isMatch = matcher.matches();
            if (!isMatch) {
                System.out.print("请填入正确的手机号 ");
            }
            return isMatch;
        }
    }


}
