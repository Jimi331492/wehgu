package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.vo.PostVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface MpPostMapper extends BaseMapper<MpPost> {

    Page<PostVO> selectPosts(Page<PostVO> page, PostPageQuery input);

    PostVO selectByUID(String postUID);
}
