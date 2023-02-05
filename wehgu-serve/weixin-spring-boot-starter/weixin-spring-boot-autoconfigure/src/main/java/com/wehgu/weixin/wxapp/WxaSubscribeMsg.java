package com.wehgu.weixin.wxapp;

import com.jfinal.weixin.sdk.utils.JsonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class WxaSubscribeMsg implements Serializable {

    private String touser;
    private String template_id;
    private String page;
    private TemplateItem data;

    private String miniprogram_state;

    private String lang;

    public WxaSubscribeMsg(){
        this.data= new TemplateItem();
    }






    /**
     * 直接转化成jsonString
     * @return {String}
     */
    public String build() {
        return JsonUtils.toJson(this);
    }


    public static class TemplateItem extends HashMap<String, Item> {
        public TemplateItem() {}

        public TemplateItem(String key, Item item) {
            this.put(key, item);
        }
    }

    @Data
    public static class Item {

        public Item (){

        }

        public Item(Object value) {
            this(value, "#999");
        }
        public Item(Object value, String color) {
            this.value = value;
            this.color = color;
        }

        private Object value;
        private String color;


    }
}