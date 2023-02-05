package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpNotice;
import com.wehgu.admin.entities.query.NoticePageQuery;
import com.wehgu.admin.entities.vo.NoticeVO;
import com.wehgu.admin.mapper.MpNoticeMapper;
import com.wehgu.admin.service.IMpNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
@Service
public class MpNoticeServiceImpl extends ServiceImpl<MpNoticeMapper, MpNotice> implements IMpNoticeService {

    @Resource
    private MpNoticeMapper mpNoticeMapper;

    @Override
    public Page<NoticeVO> selectNotices(Page<NoticeVO> page, NoticePageQuery input) {
        return mpNoticeMapper.selectNotices(page,input);
    }
}
