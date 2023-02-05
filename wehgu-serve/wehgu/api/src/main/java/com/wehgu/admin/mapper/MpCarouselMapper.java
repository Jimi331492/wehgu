package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpCarousel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.CarousePageQuery;
import com.wehgu.admin.entities.vo.CarouseVO;

/**
 * <p>
 * 轮播图表 Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-26
 */
public interface MpCarouselMapper extends BaseMapper<MpCarousel> {

    Page<CarouseVO> selectCarousels(Page<CarouseVO> page, CarousePageQuery input);
}
