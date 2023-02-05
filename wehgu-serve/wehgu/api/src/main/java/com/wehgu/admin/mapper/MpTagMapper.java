package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.TagPageQuery;
import com.wehgu.admin.entities.vo.TagVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-07
 */
public interface MpTagMapper extends BaseMapper<MpTag> {

    Page<TagVO> selectTags(Page<TagVO> page, TagPageQuery input);
}
