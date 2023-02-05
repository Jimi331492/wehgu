package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpCarousel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.CarousePageQuery;
import com.wehgu.admin.entities.vo.CarouseVO;

/**
 * <p>
 * 轮播图表 服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-26
 */
public interface IMpCarouselService extends IService<MpCarousel> {

    Page<CarouseVO> selectCarousels(Page<CarouseVO> page, CarousePageQuery input);
}
