package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.dto.LikedCountDTO;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;
import com.wehgu.admin.mapper.MpCommentMapper;
import com.wehgu.admin.service.IMpCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.utils.OSS.OSSUtil;
import com.wehgu.admin.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@Service
public class MpCommentServiceImpl extends ServiceImpl<MpCommentMapper, MpComment> implements IMpCommentService {

    @Resource
    private MpCommentMapper mpCommentMapper;

    @Resource
    private IRedisService iRedisService;

    @Resource
    private OSSUtil ossUtil;

    @Override
    public Page<CommentVO> selectComments(Page<CommentVO> page, CommentPageQuery input) {
        Page<CommentVO> comments=mpCommentMapper.selectComments(page,input);
        List<CommentVO> list=comments.getRecords();

        StarPageQuery query=new StarPageQuery();
        query.setType(1);
        //统计redis中的点赞数
        HashMap<String,Integer> commentStarCountInRedis=iRedisService.queryStarCountFromRedis(query);

        for (CommentVO item:list) {
            String linkedUuid=item.getCommentUuid();
            if(commentStarCountInRedis.containsKey(linkedUuid)){
                item.setStar(item.getStar()+commentStarCountInRedis.get(linkedUuid));
            }
            //如果评论存在图片
            if(StringUtils.isNotBlank(item.getPath())){
                boolean exitsObject = ossUtil.isExitsObject(item.getPath());
                if(!exitsObject){
                    item.setURI("/404.png");
                }
                String URI = ossUtil.generatePresignedUrl(item.getPath(), 10);
                item.setURI(URI);
            }
        }

        comments.setRecords(list);

        return comments;
    }
}
