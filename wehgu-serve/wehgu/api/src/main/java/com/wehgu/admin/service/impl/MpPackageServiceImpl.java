package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpPackage;
import com.wehgu.admin.entities.query.PackagePageQuery;
import com.wehgu.admin.entities.vo.PackageVO;
import com.wehgu.admin.mapper.MpPackageMapper;
import com.wehgu.admin.service.IMpPackageService;
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
public class MpPackageServiceImpl extends ServiceImpl<MpPackageMapper, MpPackage> implements IMpPackageService {

    @Resource
    private MpPackageMapper mpPackageMapper;

    @Override
    public Page<PackageVO> selectPackages(Page<PackageVO> page, PackagePageQuery input) {
        return mpPackageMapper.selectPackages(page,input);
    }
}
