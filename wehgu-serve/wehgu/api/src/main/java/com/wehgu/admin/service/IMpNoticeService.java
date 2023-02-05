package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.NoticePageQuery;
import com.wehgu.admin.entities.vo.NoticeVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface IMpNoticeService extends IService<MpNotice> {

    Page<NoticeVO> selectNotices(Page<NoticeVO> page, NoticePageQuery input);
}
