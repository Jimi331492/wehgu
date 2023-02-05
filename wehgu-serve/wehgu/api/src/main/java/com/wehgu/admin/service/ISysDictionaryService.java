package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysDictionary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wehgu.admin.entities.query.DictionaryPageQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface ISysDictionaryService extends IService<SysDictionary> {

    Page<SysDictionary> selectDictionary(Page<SysDictionary> page, DictionaryPageQuery input);
}
