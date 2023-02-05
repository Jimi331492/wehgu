package com.wehgu.weixin.common;

/**
 * <p> 响应码枚举 - 可参考HTTP状态码的语义 </p>
 *
 */

public enum ResultCode {
    //成功
    SUCCESS(200, "SUCCESS"),
    //失败
    FAILURE(400, "FAILURE"),
    // 未登录
    UN_LOGIN(400, "未登录"),
    //未认证（签名错误、token错误）
    UNAUTHORIZED(403, "未认证或Token失效"),
    //未通过认证
    USER_UNAUTHORIZED(402, "用户名或密码不正确"),
    //接口不存在
    NOT_FOUND(404, "接口或资源不存在"),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 描述
     */
    private String description;

    ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}