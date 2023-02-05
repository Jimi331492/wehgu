package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpCarousel;
import com.wehgu.admin.entities.query.CarousePageQuery;
import com.wehgu.admin.entities.vo.CarouseVO;
import com.wehgu.admin.mapper.MpCarouselMapper;
import com.wehgu.admin.service.IMpCarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.utils.OSS.OSSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 轮播图表 服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-26
 */
@Service
public class MpCarouselServiceImpl extends ServiceImpl<MpCarouselMapper, MpCarousel> implements IMpCarouselService {

    @Resource
    MpCarouselMapper mpCarouselMapper;

    @Resource
    private OSSUtil ossUtil;

    @Override
    public Page<CarouseVO> selectCarousels(Page<CarouseVO> page, CarousePageQuery input) {
        Page<CarouseVO> carousePage=mpCarouselMapper.selectCarousels(page, input);
        for (CarouseVO item:carousePage.getRecords()) {
            if(StringUtils.isNotBlank(item.getPath())){
                boolean exitsObject = ossUtil.isExitsObject(item.getPath());
                if(!exitsObject){
                    item.setURI("/assets/404.png");
                }else{
                    String URI = ossUtil.generatePresignedUrl(item.getPath(), 5);
                    item.setURI(URI);
                }
            }

        }
        return carousePage;
    }
}
