package com.wehgu.weixin.service;

import com.wehgu.weixin.entity.wxpay.AdvancePayResInfo;

public interface IWxPayUpdateDBService {

    boolean updateDbInfo(AdvancePayResInfo payResInfo);
}