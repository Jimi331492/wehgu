package com.wehgu.weixin.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResultTemplate {

    /**
     * 响应码：参考`ResultCode`
     */
    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "响应消息")
    private String message;

    /**
     * 响应中的数据
     */
    @ApiModelProperty(value = "响应数据")
    @JSONField(name = "data")
    @JsonProperty(value = "data")
    private Object data;

    public ResultTemplate() {
    }

    public ResultTemplate(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultTemplate(Object data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = "操作成功!";
        this.data = data;
    }

    public ResultTemplate(String message) {
        this(ResultCode.SUCCESS.getCode(), message, null);
    }

    public ResultTemplate(String message, Integer code) {
        this.code = code;
        this.message = message;
    }

    public ResultTemplate(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /***
     * 过期
     */
    public static ResultTemplate expired(String message) {
        return new ResultTemplate(ResultCode.UN_LOGIN.getCode(), message, null);
    }

    /***
     * 自定义错误提示信息
     */
    public static ResultTemplate err(String message) {
        return new ResultTemplate(ResultCode.FAILURE.getCode(), message, null);
    }

    /***
     * 自定义错误返回码和错误提示信息
     */
    public static ResultTemplate err(Integer code, String message) {
        return new ResultTemplate(code, message, null);
    }



    /***
     * 成功默认返回码
     */
    public static ResultTemplate ok() {
        return new ResultTemplate(ResultCode.SUCCESS.getCode(), "操作成功!", null);
    }

    /***
     * 自定义返回提示信息
     */
    public static ResultTemplate ok(String message) {
        return new ResultTemplate(ResultCode.SUCCESS.getCode(), message, null);
    }

    /***
     * 自定义返回提示信息
     */
    public static <T> ResultTemplate ok(T object) {
        ResultTemplate res=ResultTemplate.ok(null);
        if(object!=null){
            res.data=object;
        }
        return res;
    }


    /**
     * 自定义成功返回码和成功提示信息
     */
    public static ResultTemplate ok(Integer code, String message) {
        return new ResultTemplate(code, message);
    }

    /**
     * 自定义
     */
    public static ResultTemplate ok(Integer code, String message, Object data) {
        return new ResultTemplate(code, message, data);
    }


}