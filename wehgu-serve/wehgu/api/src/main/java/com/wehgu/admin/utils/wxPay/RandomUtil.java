package com.wehgu.admin.utils.wxPay;

public class RandomUtil {
    public String getRandomCode(int codeLen) {
        java.util.Random randomCode = new java.util.Random();
        String strCode = "";
        while (codeLen > 0) {
            int charCode = randomCode.nextInt(9);
            strCode += charCode;
            codeLen--;
        }
        return strCode;
    }
}