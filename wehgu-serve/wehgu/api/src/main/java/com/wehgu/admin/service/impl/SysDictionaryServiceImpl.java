package com.wehgu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysDictionary;
import com.wehgu.admin.entities.query.DictionaryPageQuery;
import com.wehgu.admin.mapper.SysDictionaryMapper;
import com.wehgu.admin.service.ISysDictionaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements ISysDictionaryService {

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

    @Override
    public Page<SysDictionary> selectDictionary(Page<SysDictionary> page, DictionaryPageQuery input) {
        return sysDictionaryMapper.selectDictionary(page,input);
    }
}
