package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpUserLocation;
import com.wehgu.admin.entities.query.LocationPageQuery;
import com.wehgu.admin.entities.vo.LocationVO;
import com.wehgu.admin.mapper.MpUserLocationMapper;
import com.wehgu.admin.service.IMpUserLocationService;
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
public class MpUserLocationServiceImpl extends ServiceImpl<MpUserLocationMapper, MpUserLocation> implements IMpUserLocationService {

    @Resource
    private MpUserLocationMapper mpUserLocationMapper;


    @Override
    public Page<LocationVO> selectLocations(Page<LocationVO> page, LocationPageQuery input) {
        return mpUserLocationMapper.selectLocations(page,input);
    }
}
