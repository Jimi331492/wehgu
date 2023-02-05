package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.MpPackage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.PackagePageQuery;
import com.wehgu.admin.entities.vo.PackageVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-04-26
 */
public interface MpPackageMapper extends BaseMapper<MpPackage> {

    Page<PackageVO> selectPackages(Page<PackageVO> page, PackagePageQuery input);
}
