package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface MpCommentMapper extends BaseMapper<MpComment> {

    Page<CommentVO> selectComments(Page<CommentVO> page, CommentPageQuery input);
}
