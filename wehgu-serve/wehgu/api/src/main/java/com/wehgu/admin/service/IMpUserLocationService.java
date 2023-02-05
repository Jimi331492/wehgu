package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpUserLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.LocationPageQuery;
import com.wehgu.admin.entities.vo.LocationVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface IMpUserLocationService extends IService<MpUserLocation> {

    Page<LocationVO> selectLocations(Page<LocationVO> page, LocationPageQuery input);
}
