package com.wehgu.admin.service.impl;


import com.wehgu.admin.common.LikedStatusEnum;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.dto.LikedCountDTO;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.service.IMpStarService;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.Cursor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public void saveStarToRedis(MpStar star) {
        String key = RedisKeyUtils.getStarKey(star.getType(),star.getUserDetailUuid(),star.getLinkedUuid());
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_STAR, key, star);
    }


    @Override
    public void deleteStarFromRedis(String starUuid) {
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_STAR, starUuid);
    }

    @Override
    public void incrementStarNum(Integer type,String linkedUuid) {
        String key = RedisKeyUtils.getCountKey(type,linkedUuid);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_STAR_COUNT, key, 1);
    }

    @Override
    public void decrementStarNum(Integer type,String linkedUuid) {
        String key = RedisKeyUtils.getCountKey(type,linkedUuid);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_STAR_COUNT, key, -1);
    }

    @Override
    public HashMap<String,Object> queryStarFromRedis(StarPageQuery input) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_STAR, ScanOptions.NONE);
        HashMap<String,Object> map = new HashMap<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            MpStar star = (MpStar) entry.getValue();

            if(StringUtils.isNotBlank(input.getLinkedUuid())){
                //不相等 找下一个
                if(!star.getLinkedUuid().equals(input.getLinkedUuid())){
                    continue;
                }
            }

            if(StringUtils.isNotBlank(input.getUserDetailUuid()))  {
                //不相等 找下一个
                if(!star.getUserDetailUuid().equals(input.getUserDetailUuid())){
                    continue;
                }
            }

            if(input.getType()!=null){
                //不相等 找下一个
                if(!star.getType().equals(input.getType())){
                    continue;
                }
            }

            if(input.getStatus()!=null){
                //不相等 找下一个
                if(!star.getStatus().equals(input.getStatus())){
                    continue;
                }
            }

            map.put((String) entry.getKey(),star);//找到了
        }
        return map;
    }

    @Override
    public List<MpStar> getStarFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_STAR, ScanOptions.NONE);
        List<MpStar> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();

            MpStar star = (MpStar) entry.getValue();

            list.add(star);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_STAR, key);
        }

        return list;

    }

    @Override
    public List<LikedCountDTO> getStarCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_STAR_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 StarCountDT
            String key = (String) map.getKey();
            Integer value =(Integer) map.getValue();
            String[] split = key.split("::");
            Integer type = Integer.valueOf(split[0]);
            String linkedUuid = split[1];
            LikedCountDTO dto = new LikedCountDTO();
            dto.setType(type);
            dto.setLinkedUuid(linkedUuid);
            dto.setStarNum(value);

            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_STAR_COUNT, key);

        }
        return list;
    }

    @Override
    public HashMap<String,Integer> queryStarCountFromRedis(StarPageQuery input) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_STAR_COUNT, ScanOptions.NONE);
        HashMap<String,Integer> starCountMap = new HashMap<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 StarCountDT
            String key = (String) map.getKey();
            Integer value =(Integer) map.getValue();
            String[] split = key.split("::");
            Integer type = Integer.valueOf(split[0]);
            String linkedUuid = split[1];

            if(input.getType()!=null){
                //不相等 找下一个
                if(!type.equals(input.getType())){
                    continue;
                }
            }

            if(StringUtils.isNotBlank(input.getLinkedUuid())){
                //不相等 找下一个
                if(!linkedUuid.equals(input.getLinkedUuid())){
                    continue;
                }
            }


            starCountMap.put(linkedUuid,value);
        }
        return starCountMap;
    }
}
