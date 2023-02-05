package com.wehgu.admin.utils.SMS;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SmsParams {
    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 手机号码
     */
    private String telephone;


    public SmsParams(String telephone, String verifyCode) {
        this.telephone = telephone;
        this.verifyCode = verifyCode;
    }
}