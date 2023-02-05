package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpTag;
import com.wehgu.admin.entities.query.TagPageQuery;
import com.wehgu.admin.entities.vo.TagVO;
import com.wehgu.admin.mapper.MpTagMapper;
import com.wehgu.admin.service.IMpTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-07
 */
@Service
public class MpTagServiceImpl extends ServiceImpl<MpTagMapper, MpTag> implements IMpTagService {

    @Resource
    MpTagMapper mpTagMapper;

    @Override
    public Page<TagVO> selectTags(Page<TagVO> page, TagPageQuery input) {
        return mpTagMapper.selectTags(page,input);
    }
}
