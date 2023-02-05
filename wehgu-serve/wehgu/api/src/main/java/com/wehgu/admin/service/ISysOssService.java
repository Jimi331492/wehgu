package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysOss;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.OSSAuthPageQuery;
import com.wehgu.admin.entities.query.OssPageQuery;
import com.wehgu.admin.entities.vo.AuthVO;
import com.wehgu.admin.entities.vo.OssAuthVO;
import com.wehgu.admin.entities.vo.OssVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-12
 */
public interface ISysOssService extends IService<SysOss> {

    Page<OssVO> selectOss(Page<OssVO> page, OssPageQuery input);

    List<AuthVO> selectOssAuth(OSSAuthPageQuery input);
}
