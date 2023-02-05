package com.wehgu.weixin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TemplateMsg {
    private String touser;

    private String template_id;

    private String url;

    private Minipropram miniprogram;

    private Map<String,TemplateMsgData> data;
}