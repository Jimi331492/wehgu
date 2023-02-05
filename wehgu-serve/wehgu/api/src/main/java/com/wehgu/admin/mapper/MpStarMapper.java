package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.StarVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface MpStarMapper extends MppBaseMapper<MpStar> {

    Page<StarVO> selectStars(Page<StarVO> page, StarPageQuery input);
}
