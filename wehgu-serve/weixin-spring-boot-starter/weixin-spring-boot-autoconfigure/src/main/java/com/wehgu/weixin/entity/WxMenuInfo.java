package com.wehgu.weixin.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class WxMenuInfo {
    private String wx_GZH_appId;
    private WxMenuRoot wxMenuRoot;
}