package com.wehgu.weixin.entity.wxpay;

import lombok.Data;

@Data
public class PreyPayRes {

    private AdvancePayInfo advancePayInfo;

    private String wxClientPayJsonStr;
}