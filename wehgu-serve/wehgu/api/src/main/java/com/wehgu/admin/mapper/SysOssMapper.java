package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysOss;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.OSSAuthPageQuery;
import com.wehgu.admin.entities.query.OssPageQuery;
import com.wehgu.admin.entities.vo.OssAuthVO;
import com.wehgu.admin.entities.vo.OssVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-12
 */
public interface SysOssMapper extends BaseMapper<SysOss> {

    Page<OssVO> selectOss(Page<OssVO> page, OssPageQuery input);

    List<OssAuthVO> groupByUID(@Param(value = "input") OSSAuthPageQuery input);
}
