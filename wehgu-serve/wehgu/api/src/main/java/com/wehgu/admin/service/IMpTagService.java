package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.TagPageQuery;
import com.wehgu.admin.entities.vo.TagVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-07
 */
public interface IMpTagService extends IService<MpTag> {

    Page<TagVO> selectTags(Page<TagVO> page, TagPageQuery input);
}
