package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpTag;
import com.wehgu.admin.entities.SysDictionary;
import com.wehgu.admin.entities.dto.DictionaryDTO;
import com.wehgu.admin.entities.dto.TagDTO;
import com.wehgu.admin.entities.query.DictionaryPageQuery;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.vo.PostVO;
import com.wehgu.admin.service.ISysDictionaryService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@RestController
@Api(tags = "字典管理-接口")
@RequestMapping("/dictionary")
public class SysDictionaryController {

    @Resource
    private ISysDictionaryService iSysDictionaryService;

    @PostMapping(value = "/getDictionaryPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取字典", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取字典")
    public ResultTemplate getDictionaryPage(@RequestBody DictionaryPageQuery input) {

        Page<SysDictionary> page = new Page<>(input.getPage(), input.getLimit());

        Page<SysDictionary> result = iSysDictionaryService.selectDictionary(page, input);

        return ResultTemplate.ok(200, "获取字典成功", result);
    }

    @PostMapping(value = "/saveDictionary", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存键对值", httpMethod = "POST", response = ResultTemplate.class, notes = "保存键对值")
    public ResultTemplate saveDictionary(@RequestBody DictionaryDTO input) {

        Integer id = input.getId();

        if (id == null || id < 0) {
            //新增
            EmptyUtil.isEmpty(input.getKey(), "key不能为空");
            EmptyUtil.isEmpty(input.getValueList(), "value不能为空");

            for (String item : input.getValueList()) {
                SysDictionary sysDictionary = new SysDictionary();
                sysDictionary.setKey(input.getKey());
                sysDictionary.setValue(item);
                EmptyUtil.bool(iSysDictionaryService.save(sysDictionary), "新增失败");
            }

        } else {//修改
            SysDictionary sysDictionary = new SysDictionary();
            BeanUtil.copyProperties(input, sysDictionary);
            EmptyUtil.bool(iSysDictionaryService.updateById(sysDictionary), "编辑失败");
        }


        return ResultTemplate.ok("操作成功");
    }

    @DeleteMapping(value = "/deleteDictionary", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除字典接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除字典接口")
    public ResultTemplate deleteDictionary(@RequestBody List<Integer> input) {

        EmptyUtil.isEmpty(input, "参数为空");

        EmptyUtil.bool(
                iSysDictionaryService.removeByIds(input), "删除失败");


        return ResultTemplate.ok("删除成功");
    }
}
