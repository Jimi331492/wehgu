package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface IMpCommentService extends IService<MpComment> {

    Page<CommentVO> selectComments(Page<CommentVO> page, CommentPageQuery input);
}
