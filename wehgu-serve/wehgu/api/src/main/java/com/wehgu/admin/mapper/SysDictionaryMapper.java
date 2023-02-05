package com.wehgu.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysDictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wehgu.admin.entities.query.DictionaryPageQuery;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface SysDictionaryMapper extends BaseMapper<SysDictionary> {

    Page<SysDictionary> selectDictionary(Page<SysDictionary> page, DictionaryPageQuery input);
}
