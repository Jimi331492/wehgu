package com.wehgu.admin.service;



import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.dto.LikedCountDTO;
import com.wehgu.admin.entities.query.StarPageQuery;

import java.util.HashMap;
import java.util.List;

public interface IRedisService {

    /**
     * 用户点赞。
     * @param star
     */
    void saveStarToRedis(MpStar star);


    /**
     * 从Redis中删除一条点赞数据
     * @param starUuid
     */
    void deleteStarFromRedis(String starUuid);


    /**
     * 该帖子的点赞数加1
     * @param linkedUuid
     */
    void incrementStarNum(Integer type,String linkedUuid);

    /**
     * 该用户的点赞数减1
     * @param linkedUuid
     */
    void decrementStarNum(Integer type,String linkedUuid);

    /**
     * 从redis查询点赞记录
     * @param input
     */
    HashMap<String,Object> queryStarFromRedis(StarPageQuery input);

    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<MpStar> getStarFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<LikedCountDTO> getStarCountFromRedis();

    /**
     * 从redis查询点赞数
     * @param input
     */
    HashMap<String,Integer> queryStarCountFromRedis(StarPageQuery input);


}
