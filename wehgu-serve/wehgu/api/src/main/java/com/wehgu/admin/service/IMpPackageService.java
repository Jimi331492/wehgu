package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpPackage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.PackagePageQuery;
import com.wehgu.admin.entities.vo.PackageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface IMpPackageService extends IService<MpPackage> {

    Page<PackageVO> selectPackages(Page<PackageVO> page, PackagePageQuery input);
}
