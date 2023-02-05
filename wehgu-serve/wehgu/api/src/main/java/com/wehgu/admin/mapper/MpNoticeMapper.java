package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.NoticePageQuery;
import com.wehgu.admin.entities.vo.NoticeVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface MpNoticeMapper extends BaseMapper<MpNotice> {

    Page<NoticeVO> selectNotices(Page<NoticeVO> page, NoticePageQuery input);
}
