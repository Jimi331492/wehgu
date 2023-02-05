package com.wehgu.admin.utils;

public class RedisKeyUtils {

    //保存点赞数据的key
    public static final String MAP_KEY_STAR = "MAP_KEY_STAR";
    //保存被点赞数量的key
    public static final String MAP_KEY_STAR_COUNT = "MAP_KEY_STAR_COUNT";

    /**
     * 拼接点赞的帖子或评论uid和点赞的用户的uid作为key。格式 post::a::b
     * @param type 点赞评论还是帖子 0 post 1 comment
     * @param linkedUuid 被点赞的帖子或评论的uid
     * @param userDetailUuid 点赞的用户的uid
     */public static String getStarKey(Integer type,String userDetailUuid, String linkedUuid){
        return type+"::"+userDetailUuid +
                "::" +
                linkedUuid;
    }

    /**
     *
     * @param type 点赞评论还是帖子 0 post 1 comment
     * @param linkedUuid
     * @return
     */
    public static String getCountKey(Integer type, String linkedUuid) {
        return type+
                "::" +
                linkedUuid;
    }
}
