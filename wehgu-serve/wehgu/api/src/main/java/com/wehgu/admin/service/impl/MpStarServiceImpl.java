package com.wehgu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.MpPost;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.dto.LikedCountDTO;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.StarVO;
import com.wehgu.admin.mapper.MpStarMapper;
import com.wehgu.admin.service.IMpCommentService;
import com.wehgu.admin.service.IMpPostService;
import com.wehgu.admin.service.IMpStarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.stream.events.Comment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@Service
public class MpStarServiceImpl extends MppServiceImpl<MpStarMapper, MpStar> implements IMpStarService {

    @Resource
    private MpStarMapper mpStarMapper;

    @Resource
    private IRedisService iRedisService;

    @Resource
    private IMpPostService iMpPostService;

    @Resource
    private IMpCommentService iMpCommentService;

    @Override
    public Page<StarVO> selectStars(Page<StarVO> page, StarPageQuery input) {
        Page<StarVO> starVOPage=mpStarMapper.selectStars(page,input);
        //查找Redis中所有的
        HashMap<String,Object> allStarInRedis=iRedisService.queryStarFromRedis(new StarPageQuery());

        List<StarVO> starInDB=starVOPage.getRecords();

        for (StarVO item:starInDB) {
            String key= RedisKeyUtils.getStarKey(item.getType(),item.getUserDetailUuid(),item.getLinkedUuid());
            if(allStarInRedis.containsKey(key)){
                //为什么要remove 因为redis中如果有新的 不管和数据库的一不一样 都以redis的为准
                starInDB.remove(item);
            }
        }

        for (Map.Entry<String,Object> item:allStarInRedis.entrySet()) {
            MpStar mpStar=(MpStar)item.getValue();
            StarVO starVO=new StarVO();
            BeanUtil.copyProperties(mpStar,starVO);
            if(input.getStatus()==1||input.getStatus()==0){

                if(starVO.getStatus().equals(input.getStatus())){
                    starInDB.add(starVO);
                }
            }else{
                starInDB.add(starVO);
            }
        }

        starVOPage.setRecords(starInDB);

        return starVOPage;
    }

    @Override
    @Transactional
    public MpStar saveOne(MpStar mpStar) {
        return null;
    }

    @Override
    @Transactional
    public boolean saveAll(List<MpStar> list) {
        return false;
    }

    @Override
    public HashMap<String,Object> queryStarFromRedis(StarPageQuery input) {

        return iRedisService.queryStarFromRedis(input);
    }


    @Override
    @Transactional
    public void saveStarFromRedisToDB() {
        List<MpStar> list = iRedisService.getStarFromRedis();
        for (MpStar item : list) {

            if(!saveOrUpdateByMultiId(item)){
                log.warn(item.getType()+":"+item.getUserDetailUuid()+":"+item.getLinkedUuid()+"保存到数据库失败");
            }

        }

    }

    @Override
    @Transactional
    public void saveStarNumFromRedisToDB() {
        List<LikedCountDTO> list = iRedisService.getStarCountFromRedis();
        for (LikedCountDTO dto : list) {
            if(dto.getType()==0) {
                MpPost post = iMpPostService.getOne(new QueryWrapper<MpPost>()
                        .eq("post_uuid",dto.getLinkedUuid())
                        .last("LIMIT 1"));
                //点赞数量属于无关紧要的操作，出错无需抛异常
                if (post != null){
                    Integer starNum = post.getStarNum() + dto.getStarNum();
                    post.setStarNum(starNum);
                    //更新点赞数量
                    iMpPostService.updateById(post);
                }


            }
            if(dto.getType()==1) {
                MpComment comment = iMpCommentService.getOne(new QueryWrapper<MpComment>()
                        .eq("comment_uuid",dto.getLinkedUuid())
                        .last("LIMIT 1"));
                //点赞数量属于无关紧要的操作，出错无需抛异常
                if (comment != null){
                    Integer starNum = comment.getStarNum() + dto.getStarNum();
                    comment.setStarNum(starNum);
                    //更新点赞数量
                    iMpCommentService.updateById(comment);
                }
            }
        }
    }

}
