package com.wehgu.weixin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxMenuSub implements Serializable {
    private String type;
    private String name;
    private String url;
    private String key;
    private String appid;
    private String pagepath;

    private Boolean isUrlToAuthrize=true;
}