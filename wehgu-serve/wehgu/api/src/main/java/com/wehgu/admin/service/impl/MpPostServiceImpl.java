package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.MpPost;
import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;
import com.wehgu.admin.entities.vo.PostVO;
import com.wehgu.admin.mapper.MpPostMapper;
import com.wehgu.admin.service.IMpCommentService;
import com.wehgu.admin.service.IMpPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.service.ISysOssService;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.OSS.ImageUtil;
import com.wehgu.admin.utils.OSS.OSSUtil;
import com.wehgu.admin.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class MpPostServiceImpl extends ServiceImpl<MpPostMapper, MpPost> implements IMpPostService {

    @Resource
    private MpPostMapper mpPostMapper;

    @Resource
    private IMpCommentService iMpCommentService;


    @Resource
    private ISysOssService iSysOssService;

    @Resource
    private IRedisService iRedisService;

    @Resource
    private OSSUtil ossUtil;

    @Override
    public Page<PostVO> selectPosts(Page<PostVO> page, PostPageQuery input) {
        Page<PostVO> result =mpPostMapper.selectPosts(page,input);

        //获取评论树 和 照片树
        List<PostVO> postList=result.getRecords();

        //统计redis中的类型为帖子点赞的点赞数
        StarPageQuery queryStarCountInRedis=new StarPageQuery();
        queryStarCountInRedis.setType(0);
        //统计redis中的点赞数
        HashMap<String,Integer> postStarCountInRedis=iRedisService.queryStarCountFromRedis(queryStarCountInRedis);
        //获取评论树
        for (PostVO item:postList) {
            //合计点赞数
            String linkedUuid=item.getPostUuid();
            if(postStarCountInRedis.containsKey(linkedUuid)){
                item.setStar(item.getStar()+postStarCountInRedis.get(linkedUuid));
            }

            //获取评论树
            CommentPageQuery query =new CommentPageQuery();
            query.setPostUuid(item.getPostUuid());
            Page<CommentVO> commentPage = new Page<>(query.getPage(),query.getLimit());
            Page<CommentVO> commentList=iMpCommentService.selectComments(commentPage,query);

            item.setCommentList(commentList.getRecords());

            //获取照片
            List<SysOss> sysOssList = iSysOssService.list(new QueryWrapper<SysOss>()
                    .eq("store_type_table","post")
                    .eq("relation_uuid",item.getPostUuid()));

            List<String> ImgPathList=new ArrayList<>();

            if(sysOssList!=null&&sysOssList.size()>0){
                for (SysOss bp : sysOssList) {
                    String pa = bp.getPath();
                    ImgPathList.add(pa);
                }
                String ImgIndexPath=sysOssList.get(0).getPath();

                //获取第一张图片
                if(StringUtils.isNotBlank(ImgIndexPath)){
                    boolean exitsObject = ossUtil.isExitsObject(ImgIndexPath);
                    if(!exitsObject){
                        item.setImgIndexURI("/404.png");
                    }
                    String ImgIndexURI = ossUtil.generatePresignedUrl(ImgIndexPath, 10);
                    item.setImgIndexURI(ImgIndexURI);
                }
            }

            item.setImgPathList(ImgPathList);


            //获取头像
            if(item.getAvatar()!=null
                    &&item.getAvatar().startsWith("avatar")){
                boolean exitsObject = ossUtil.isExitsObject(item.getAvatar());
                if(!exitsObject){
                    item.setAvatar("/404.png");
                }
                String URI = ossUtil.generatePresignedUrl(item.getAvatar(), 10);
                item.setAvatar(URI);
            }

        }
        return result;
    }

    @Override
    public void incrementCommentNum(String postUuid) {
        MpPost one=getOne(new QueryWrapper<MpPost>()
                .eq("post_uuid",postUuid)
                .last("LIMIT 1"));
        one.setCommentNum(one.getCommentNum()+1);
        updateById(one);
    }

    @Override
    public void decrementCommentNum(String postUuid) {
        MpPost one=getOne(new QueryWrapper<MpPost>()
                .eq("post_uuid",postUuid)
                .last("LIMIT 1"));
        one.setCommentNum(one.getCommentNum()-1);
        updateById(one);
    }

    @Override
    public PostVO selectByUID(String postUID) {
        PostVO item=mpPostMapper.selectByUID(postUID);

        //统计redis中的类型为帖子点赞的点赞数
        StarPageQuery queryStarCountInRedis=new StarPageQuery();
        queryStarCountInRedis.setType(0);
        //统计redis中的点赞数
        HashMap<String,Integer> postStarCountInRedis=iRedisService.queryStarCountFromRedis(queryStarCountInRedis);

        //合计点赞数
        String linkedUuid=item.getPostUuid();
        if(postStarCountInRedis.containsKey(linkedUuid)){
            item.setStar(item.getStar()+postStarCountInRedis.get(linkedUuid));
        }

        //获取评论树
        CommentPageQuery query =new CommentPageQuery();
        query.setPostUuid(item.getPostUuid());
        Page<CommentVO> commentPage = new Page<>(query.getPage(),query.getLimit());
        Page<CommentVO> commentList=iMpCommentService.selectComments(commentPage,query);
        item.setCommentList(commentList.getRecords());

        //获取照片
        List<SysOss> sysOssList = iSysOssService.list(new QueryWrapper<SysOss>()
                .eq("store_type_table","post")
                .eq("relation_uuid",item.getPostUuid()));

        List<String> ImgPathList=new ArrayList<>();

        if(sysOssList!=null&&sysOssList.size()>0){
            for (SysOss bp : sysOssList) {
                String pa = bp.getPath();
                ImgPathList.add(pa);
            }
            String ImgIndexPath=sysOssList.get(0).getPath();

            //获取第一张图片
            if(StringUtils.isNotBlank(ImgIndexPath)){
                boolean exitsObject = ossUtil.isExitsObject(ImgIndexPath);
                if(!exitsObject){
                    item.setImgIndexURI("/404.png");
                }
                String ImgIndexURI = ossUtil.generatePresignedUrl(ImgIndexPath, 10);
                item.setImgIndexURI(ImgIndexURI);
            }
        }

        item.setImgPathList(ImgPathList);


        //获取头像
        if(item.getAvatar()!=null
                &&item.getAvatar().startsWith("avatar")){
            boolean exitsObject = ossUtil.isExitsObject(item.getAvatar());
            if(!exitsObject){
                item.setAvatar("/404.png");
            }
            String URI = ossUtil.generatePresignedUrl(item.getAvatar(), 10);
            item.setAvatar(URI);
        }
        return item;
    }
}
