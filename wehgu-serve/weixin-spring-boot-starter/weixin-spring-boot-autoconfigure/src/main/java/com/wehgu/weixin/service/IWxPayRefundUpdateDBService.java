package com.wehgu.weixin.service;

import com.wehgu.weixin.entity.wxpay.RefundNotifyRes;

public interface IWxPayRefundUpdateDBService {
    boolean updateDbInfo(RefundNotifyRes refundNotifyRes);
}