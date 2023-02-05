package com.wehgu.weixin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxMenu implements Serializable {
    private String name;
    private String type;
    private String url;
    private String key;
    private List<WxMenuSub> sub_button;
    private String media_id;

    private Boolean isUrlToAuthrize=true;
}