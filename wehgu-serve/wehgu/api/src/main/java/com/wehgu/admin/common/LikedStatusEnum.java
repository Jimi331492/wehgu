package com.wehgu.admin.common;

import lombok.Getter;

/**
 * 用户点赞的状态
 */
@Getter
public enum LikedStatusEnum {
    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞"),
    ;

    private final Integer code;

    private final String message;

    LikedStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
