package com.wehgu.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.vo.PostVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface IMpPostService extends IService<MpPost> {

    Page<PostVO> selectPosts(Page<PostVO> page, PostPageQuery input);

    /**
     * 该帖子的评论数加1
     * @param postUuid
     */
    void incrementCommentNum(String postUuid);

    /**
     * 该用户的点赞数减1
     * @param postUuid
     */
    void decrementCommentNum(String postUuid);

    PostVO selectByUID(String post_uuid);
}
