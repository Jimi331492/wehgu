package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpUserLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.LocationPageQuery;
import com.wehgu.admin.entities.vo.LocationVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface MpUserLocationMapper extends BaseMapper<MpUserLocation> {

    Page<LocationVO> selectLocations(Page<LocationVO> page, LocationPageQuery input);
}
